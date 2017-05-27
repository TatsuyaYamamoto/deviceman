import React from 'react'
import {PropTypes} from 'prop-types'

import {KXing} from 'kxing';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import DetectorComponent from './DetectorComponent';

export default class CodeScannerDialog extends React.Component {
    static propTypes = {
        onDetectedCode: PropTypes.func.isRequired,
        open: PropTypes.bool,
        handleOpenDialog: PropTypes.func,
    };

    static defaultProps = {
        interval: 1000
    };

    onDidSnapshot = (dataImage) => {
        try {
            this.props.onDetectedCode(KXing.getReader().decode(dataImage).text);
        } catch (e) {
            // ignore
        }
    };

    render() {
        const {open, handleOpenDialog} = this.props;
        return (
            <Dialog
                title="Scan device QR code."
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={handleOpenDialog}
                    />
                ]}
                bodyStyle={{
                    padding: '0px 24px 350px'
                }}
                modal={false}
                open={open}
                onRequestClose={handleOpenDialog}>

                <DetectorComponent
                    width={400}
                    onDidSnapshot={this.onDidSnapshot}/>
            </Dialog>
        )
    }
}
