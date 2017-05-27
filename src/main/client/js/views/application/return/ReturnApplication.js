import React from 'react'

import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import ArrowBack from 'material-ui/svg-icons/navigation/arrow-back';
import Help from 'material-ui/svg-icons/action/help-outline';
import RaisedButton from 'material-ui/RaisedButton';
import Subheader from 'material-ui/Subheader';
import Paper from 'material-ui/Paper';
import {List, ListItem} from 'material-ui/List';
import LinearProgress from 'material-ui/LinearProgress';
import PersonIcon from 'material-ui/svg-icons/social/person';
import DeviceIcon from 'material-ui/svg-icons/hardware/smartphone';

import CodeScannerDialog from '../lending/component/CodeScannerDialog'
import * as DeviceService from '../../../services/DeviceService'
import SuccessReturnDialog from './component/SuccessReturnDialog'
import ErrorReturnDialog from './component/ErrorReturnDialog'

const TextResources = {
    button: {
        Apply: "Apply",
    }
};

export default class LendingApplication extends React.Component {
    getStyles() {
        return {
            applicationPaper: {
                margin: 50,
                paddingRight: 50,
                paddingLeft: 50
            },
            applyButton: {
                margin: 20
            }
        }
    }

    state = {
        isOpenCodeScanDialog: false,
        isOpenReturnSuccessDialog: false,
        isOpenReturnErrorDialog: false,
        selectedDevice: null,
        isFetching: false
    };

    handleOpenReturnSuccessDialog = () => {
        const {isOpenReturnSuccessDialog} = this.state;
        this.setState({isOpenReturnSuccessDialog: !isOpenReturnSuccessDialog})
    };

    handleOpenReturnErrorDialog = () => {
        const {isOpenReturnErrorDialog} = this.state;
        this.setState({isOpenReturnErrorDialog: !isOpenReturnErrorDialog})
    };

    handleOpenCodeScanDialog = () => {
        const {isOpenCodeScanDialog} = this.state;
        this.setState({isOpenCodeScanDialog: !isOpenCodeScanDialog})
    };

    handleSubmitApplication = () => {
        const {
            selectedDevice
        } = this.state;

        const deviceId = selectedDevice.id;

        this.setState({isFetching: true});
        DeviceService.applyReturn(deviceId)
            .then((result) => {
                switch (result) {
                    case DeviceService.SUCCESS:
                        this.handleOpenReturnSuccessDialog();
                        break;
                    case DeviceService.ERROR_DEVICE_NOT_ON_LENDING:
                        break;

                }
            })
            .then(() => {
                this.setState({isFetching: false})
            })
    };


    onDetectedCode = (text) => {
        console.info(`detected code. value: ${text}`);

        this.setState({
            isFetching: true,
            isOpenCodeScanDialog: false,
        });
        DeviceService.search(text)
            .then((devices) => {
                if (devices == null || devices.length < 1) {
                    throw new Error();
                }

                const device = devices[0];
                if (device.lending == null) {
                    throw new Error();
                }

                this.setState({
                    selectedDevice: device
                })

            })
            .catch(() => {
                this.handleOpenReturnErrorDialog();
            })
            .then(() => {
                this.setState({
                    isFetching: false
                })
            })
    };

    render() {
        const {
            applicationPaper,
            applyButton
        } = this.getStyles();

        const {
            isOpenCodeScanDialog,
            isOpenReturnSuccessDialog,
            isOpenReturnErrorDialog,
            selectedDevice,
            isFetching
        } = this.state;

        const canSubmit = selectedDevice && selectedDevice.lending;
        const borrower = selectedDevice && selectedDevice.lending ? selectedDevice.lending.user : null;

        return (
            <div>
                <AppBar
                    iconElementLeft={<IconButton><ArrowBack /></IconButton>}
                    onLeftIconButtonTouchTap={() => this.props.history.push("/")}
                    iconElementRight={<IconButton><Help /></IconButton>}
                    onRightIconButtonTouchTap={() => {
                    }}/>

                {isFetching && <LinearProgress mode="indeterminate"/>}

                <Paper style={applicationPaper}>
                    <div style={{textAlign: "center"}}>
                        <h1>Return application</h1>
                    </div>
                    <List>
                        <Subheader>Device</Subheader>
                        <div>
                            {selectedDevice ? (
                                    <ListItem
                                        key={selectedDevice.id}
                                        leftIcon={<DeviceIcon />}
                                        primaryText={`${selectedDevice.manufacturer}/${selectedDevice.name}`}
                                        onTouchTap={this.handleOpenCodeScanDialog}/>
                                ) : (
                                    <ListItem
                                        primaryText="Please select device"
                                        onTouchTap={this.handleOpenCodeScanDialog}/>
                                )}
                        </div>
                        {borrower && (
                            <div>
                                <Subheader>Borrower</Subheader>
                                <ListItem
                                    key={borrower.id}
                                    leftIcon={<PersonIcon />}
                                    primaryText={borrower.id}
                                    secondaryText={borrower.address}/>
                            </div>
                        )}
                    </List>


                </Paper>

                <RaisedButton
                    style={applyButton}
                    disabled={!canSubmit}
                    label={TextResources.button.Apply}
                    primary={true}
                    onTouchTap={this.handleSubmitApplication}/>

                <CodeScannerDialog
                    open={isOpenCodeScanDialog}
                    onDetectedCode={this.onDetectedCode}
                    handleOpenDialog={this.handleOpenCodeScanDialog}/>

                <SuccessReturnDialog
                    open={isOpenReturnSuccessDialog}
                    handleCloseDialog={this.handleOpenReturnSuccessDialog}/>
                <ErrorReturnDialog
                    open={isOpenReturnErrorDialog}
                    handleCloseDialog={this.handleOpenReturnErrorDialog}/>
            </div>
        )
    }
}
