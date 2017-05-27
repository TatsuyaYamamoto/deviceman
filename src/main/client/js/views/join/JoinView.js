import React from 'react'

import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import IconButton from 'material-ui/IconButton';
import ArrowBack from 'material-ui/svg-icons/navigation/arrow-back';
import RaisedButton from 'material-ui/RaisedButton';

import SuccessJoinDialog from './component/SuccessJoinDialog'

import * as Validator from '../../services/Validator'
import * as UserService from '../../services/UserService'

const TextResources = {
    Title: "Join Torica!",
    button: {
        Submit: "Submit",
    },
    field: {
        id: {
            label: "User ID",
            error: "半角英数字を入力してください"
        },
        address: {
            label: "Address",
            error: "アドレスでよろしく"
        }
    }
};

export default class Join extends React.Component {
    getStyles() {
        return {
            container: {
                textAlign: "center"
            },
            button: {
                margin: 10,
                width: '80%'
            }
        }
    }

    state = {
        inputValue: {
            userId: '',
            address: ''
        },
        errorMessage: {
            userId: '',
            address: '',
        },
        isOpenSuccessDialog: false
    };

    handleSubmit = () => {
        const {inputValue} = this.state;

        UserService.createUser(inputValue.userId, inputValue.address)
            .then((type) => {
                switch (type) {
                    case UserService.SUCCESS:
                        this.setState({isOpenSuccessDialog: true})
                        break;
                    case UserService.ERROR_CONFLICT:
                        const {errorMessage} = this.state;

                        this.setState({
                            errorMessage: Object.assign({}, errorMessage, {
                                userId: '登録済みのユーザーIDです'
                            })
                        });
                        break;
                    case UserService.ERROR:
                        break;

                }
            })
            .catch(function (error) {
                console.log(error);
            });
    };

    handleCloseJoinSuccessDialog = () => {
        const {isOpenSuccessDialog} = this.state;
        this.setState({isOpenSuccessDialog: !isOpenSuccessDialog});
    };

    handleUpdateUserId = (event, newValue) => {
        const {inputValue, errorMessage} = this.state;

        this.setState({
            inputValue: Object.assign({}, inputValue, {
                userId: newValue
            })
        });

        this.setState({
            errorMessage: Object.assign({}, errorMessage, {
                userId: Validator.joinUserId(newValue) ?
                    '' :
                    TextResources.field.id.error
            })
        })
    };

    handleUpdateAddress = (event, newValue) => {
        const {inputValue, errorMessage} = this.state;

        this.setState({
            inputValue: Object.assign({}, inputValue, {
                address: newValue
            })
        });

        this.setState({
            errorMessage: Object.assign({}, errorMessage, {
                address: Validator.joinUserAddress(newValue) ?
                    '' :
                    TextResources.field.address.error
            })
        })
    };

    render() {
        const {
            container,
            button
        } = this.getStyles();

        const {
            inputValue,
            errorMessage,
            isOpenSuccessDialog
        } = this.state;

        const canSubmit =
            Validator.joinUserId(inputValue.userId) &&
            Validator.joinUserAddress(inputValue.address);

        return (
            <div>
                <AppBar
                    iconElementLeft={<IconButton><ArrowBack /></IconButton>}
                    onLeftIconButtonTouchTap={() => {
                        this.props.history.push("/");
                    }}/>

                <div style={container}>
                    <h1>{TextResources.Title}</h1>
                    <span>The best way to develop, test, and manage for somebody.</span>
                    <div>
                        <TextField
                            floatingLabelText={TextResources.field.id.label}
                            errorText={errorMessage.userId}
                            onChange={this.handleUpdateUserId}/>
                        <br />
                        <TextField
                            floatingLabelText={TextResources.field.address.label}
                            errorText={errorMessage.address}
                            onChange={this.handleUpdateAddress}/>
                        <br />
                        <RaisedButton
                            disabled={!canSubmit}
                            label={TextResources.button.Submit}
                            onTouchTap={this.handleSubmit}
                            style={button}/>
                    </div>
                </div>

                <SuccessJoinDialog
                    open={isOpenSuccessDialog}
                    handleCloseDialog={this.handleCloseJoinSuccessDialog}/>
            </div>
        )
    }
}
