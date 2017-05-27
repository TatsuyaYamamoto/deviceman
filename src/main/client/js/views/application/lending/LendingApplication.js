import React from 'react'

import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import ArrowBack from 'material-ui/svg-icons/navigation/arrow-back';
import Help from 'material-ui/svg-icons/action/help-outline';
import RaisedButton from 'material-ui/RaisedButton';
import Subheader from 'material-ui/Subheader';
import Paper from 'material-ui/Paper';
import DatePicker from 'material-ui/DatePicker';
import TimePicker from 'material-ui/TimePicker';
import {List, ListItem} from 'material-ui/List';
import Checkbox from 'material-ui/Checkbox';
import LinearProgress from 'material-ui/LinearProgress';
import PersonIcon from 'material-ui/svg-icons/social/person';
import DeviceIcon from 'material-ui/svg-icons/hardware/smartphone';


import SelectUserDialog from './component/SelectUserDialog'
import CodeScannerDialog from './component/CodeScannerDialog'
import {search as searchUser} from '../../../services/UserService'
import {search as searchDevice} from '../../../services/DeviceService'

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
        returnDate: null,
        isSaveBorrower: false,
        isOpenSelectUserDialog: false,
        isOpenCodeScanDialog: false,
        users: [],
        selectedUserIndex: null,
        selectedDevice: null,
        isFetching: false
    };

    handleOpenSelectUserDialog = () => {
        const {isOpenSelectUserDialog} = this.state;
        this.setState({isOpenSelectUserDialog: !isOpenSelectUserDialog})
    };

    handleOpenCodeScanDialog = () => {
        const {isOpenCodeScanDialog} = this.state;
        this.setState({isOpenCodeScanDialog: !isOpenCodeScanDialog})
    };

    handleSearchUser = (query) => {
        this.setState({
            isFetching: true
        });
        searchUser(query)
            .then((users) => {
                this.setState({
                    users: users
                })
            })
            .catch(error => error)
            .then(() => {
                this.setState({
                    isFetching: false
                })
            })
    };

    handleSelectUser = (index) => {
        this.setState({selectedUserIndex: index});
        this.handleOpenSelectUserDialog();
    };

    onDetectedCode = (text) => {
        console.info(`detected code. value: ${text}`);

        this.setState({
            isFetching: true,
            isOpenCodeScanDialog: false,
        });
        searchDevice(text)
            .then((devices) => {
                if (devices == null || devices.length < 1) {
                    throw new Error();
                }

                this.setState({
                    selectedDevice: devices[0],
                    isFetching: false
                })

            })
            .catch(error => error)
            .then(() => {
                this.setState({
                    isFetching: false
                })
            })
    };

    onChangeReturnDate = (nullArg, newDate) => {
        const {returnDate} = this.state;
        returnDate.setFullYear(
            newDate.getFullYear(),
            newDate.getMonth(),
            newDate.getDate());

        if (Date.now() > newDate.getTime()) {
            console.error('Cannot set a date that is later then now.');
            return;
        }

        this.setState({returnDate: returnDate});
    };

    onChangeReturnTime = (nullArg, newDate) => {
        const {returnDate} = this.state;
        returnDate.setHours(
            newDate.getHours(),
            newDate.getMinutes(),
            newDate.getSeconds(),
            newDate.getMilliseconds());

        if (Date.now() > newDate.getTime()) {
            console.error('Cannot set a date that is later then now.');
            return;
        }

        this.setState({returnDate: returnDate});
    };

    componentWillMount() {
        const returnDate = new Date();
        returnDate.setDate(returnDate.getDate() + 1);
        returnDate.setHours(17, 6);

        this.setState({returnDate: returnDate});
    }

    render() {
        const {
            applicationPaper,
            applyButton
        } = this.getStyles();

        const {
            returnDate,
            isSaveBorrower,
            isOpenSelectUserDialog,
            isOpenCodeScanDialog,
            users,
            selectedUserIndex,
            selectedDevice,
            isFetching
        } = this.state;

        const canSubmit = selectedUserIndex && selectedDevice;

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
                        <h1>Lending application</h1>
                    </div>
                    <List>
                        <Subheader>User</Subheader>
                        <div>
                            {selectedUserIndex ? (
                                    <ListItem
                                        key={users[selectedUserIndex].id}
                                        leftIcon={<PersonIcon />}
                                        primaryText={users[selectedUserIndex].id}
                                        secondaryText={users[selectedUserIndex].address}
                                        onTouchTap={this.handleOpenSelectUserDialog}/>
                                ) : (
                                    <ListItem
                                        primaryText="Please select user"
                                        onTouchTap={this.handleOpenSelectUserDialog}/>
                                )}
                        </div>
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
                        <Subheader>Return date</Subheader>
                        <ListItem
                            disabled={true}
                        style={{
                            paddingTop: 0
                        }}>
                            <div style={{
                                display: 'flex'
                            }}>
                                <DatePicker
                                    value={returnDate}
                                    firstDayOfWeek={0}
                                    hintText="-" //　I don't know why but, it's required arg.
                                    onChange={this.onChangeReturnDate}/>
                                <TimePicker
                                    value={returnDate}
                                    format="ampm"
                                    hintText="-" //　I don't know why but, it's required arg.
                                    onChange={this.onChangeReturnTime}/>
                            </div>
                        </ListItem>
                    </List>


                </Paper>

                <RaisedButton
                    style={applyButton}
                    disabled={!canSubmit}
                    label={TextResources.button.Apply}
                    primary={true}/>

                <Checkbox
                    checked={isSaveBorrower}
                    label="Save borrowing user."
                    onCheck={() => this.setState({isSaveBorrower: !isSaveBorrower})}
                />
                <SelectUserDialog
                    users={users}
                    open={isOpenSelectUserDialog}
                    handleCloseDialog={this.handleOpenSelectUserDialog}
                    handleSearchUser={this.handleSearchUser}
                    handleSelectUser={this.handleSelectUser}/>

                <CodeScannerDialog
                    open={isOpenCodeScanDialog}
                    onDetectedCode={this.onDetectedCode}
                    handleOpenDialog={this.handleOpenCodeScanDialog}/>
            </div>
        )
    }
}
