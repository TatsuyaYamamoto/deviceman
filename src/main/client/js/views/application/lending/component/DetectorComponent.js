import React from 'react'
import {PropTypes} from 'prop-types'
import {KXing} from 'kxing';
import VideoStreamLoader from '../../../../services/VideoStreamLoader';

// const OVERLAY_FILL_STYLE = MuiTheme.appBar.color;
const OVERLAY_FILL_STYLE = '#303030';
const OVERLAY_ALPHA = 0.4;

export default class CameraRanderComponent extends React.Component {
    static propTypes = {
        /**
         * width of the component
         */
        width: PropTypes.number.isRequired,

        /**
         * height of the component
         */
        height: PropTypes.number,

        /**
         *
         */
        interval: PropTypes.number,

        /**
         * Fired when the `Dialog` is requested to be closed by a click outside the `Dialog` or on the buttons.
         *
         * @param {bool} buttonClicked Determines whether a button click triggered this request.
         */
        onDidSnapshot: PropTypes.func.isRequired,

        /**
         * Fired when the `Dialog` is requested to be closed by a click outside the `Dialog` or on the buttons.
         *
         * @param {object} buttonClicked Determines whether a button click triggered this request.
         */
        onErrorCapture: PropTypes.func,
    };

    static defaultProps = {
        interval: 1000
    };

    _dependencies = {
        videoStreamLoader: new VideoStreamLoader()
    };

    _timeoutId = null;

    startCapture = () => {
        // setup and start capture.
        const videoElement = this.videoElement;
        const overlayElement = this.canvasElement;

        Promise.resolve()
            .then(() => {
                return this._dependencies.videoStreamLoader.load(videoElement);
            })
            .then(() => {
                // get video element size.
                const videoDeviceRasio = videoElement.videoHeight / videoElement.videoWidth;
                const videoWidth = videoElement.width;
                const videoHeight = videoWidth * videoDeviceRasio;

                // get clip size props.
                const clipProps = this.getClipProps(videoWidth, videoHeight);

                // draw overlay
                overlayElement.width = videoWidth;
                overlayElement.height = videoHeight;
                const context = overlayElement.getContext('2d');
                context.globalAlpha = OVERLAY_ALPHA;
                context.fillStyle = OVERLAY_FILL_STYLE;
                context.fillRect(0, 0, videoWidth, videoHeight);
                context.clearRect(...clipProps);

                // setup canvas element for capture
                const captureCanvasElement = document.createElement('canvas');
                captureCanvasElement.width = videoWidth;
                captureCanvasElement.height = videoHeight;
                const captureContext = captureCanvasElement.getContext('2d');

                const capture = () => {
                    this._timeoutId = setTimeout(capture, this.props.interval);
                    captureContext.beginPath();
                    captureContext.rect(...clipProps);
                    captureContext.clip();
                    captureContext.drawImage(videoElement, 0, 0, videoWidth, videoHeight);

                    this.props.onDidSnapshot(captureContext.getImageData(...clipProps));
                };
                capture();

            })
            .catch(this.props.onErrorCapture);
    };

    stopCapture = () => {
        this._dependencies.videoStreamLoader.stop();
        clearTimeout(this._timeoutId);
        this._timeoutId = null;
    };

    getClipProps(videoWidth, videoHeight) {
        const captureDimension = videoWidth < videoHeight ? videoWidth / 1.5 : videoHeight / 1.5;
        const captureX = videoWidth * 0.5 - captureDimension * 0.5;
        const captureY = videoHeight * 0.5 - captureDimension * 0.5;

        return [captureX, captureY, captureDimension, captureDimension];
    }

    getStyles() {
        return {
            cameraWindow: {
                position: 'relative'
            },
            video: {
                position: 'absolute'
            },
            frame: {
                position: 'absolute',
                zIndex: 1,
            }
        }
    }

    handleVideoElementRef = (element) => {
        if (element != null) {
            this.videoElement = element;
        }
    };

    handleCanvasElementRef = (element) => {
        if (element != null) {
            this.canvasElement = element;
        }
    };

    componentDidMount() {
        window.addEventListener('focus', this.startCapture);
        window.addEventListener('blur', this.stopCapture);

        this.startCapture();
    }

    componentWillUnmount() {
        window.removeEventListener('focus', this.startCapture);
        window.removeEventListener('blur', this.stopCapture);

        this.stopCapture();
    }

    componentWillReceiveProps(nextProps) {
        // handle resize event.
        if (this.props.width != nextProps.width) {
            this.stopCapture();
            this.startCapture();
        }
    }

    render() {
        const {cameraWindow, video, frame} = this.getStyles();
        const {width} = this.props;

        return (
            <div style={cameraWindow}>
                <video
                    autoPlay
                    style={video}
                    width={width}
                    ref={this.handleVideoElementRef}/>
                <canvas
                    style={frame}
                    ref={this.handleCanvasElementRef}/>
            </div>
        )
    }
}
