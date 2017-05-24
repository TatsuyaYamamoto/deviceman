import React from 'react'

import {browserHistory} from 'react-router'

import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';

import {MuiTheme} from '../../Theme';

const TextResources = {
    Title: "Torica",
    Subtitle: "Toriaezu tanmatsu no kashidashi Rireki wo nokoshite okouCa.",
    button: {
        Rental: "借り出し申請をする",
        Return: "返却申請",
        Join: "ユーザーの新規登録",
        List: "ユーザー・端末・貸出状況照会",
        Manage: "リトルデーモンモード"
    }
};


export default class TopView extends React.Component {
    getStyles() {
        return {
            document: {
                backgroundColor: MuiTheme.appBar.color
            },
            container: {
                textAlign: "center"
            },
            button: {
                margin: 10,
                width: '80%'
            }
        }
    }

    render() {
        const {
            container,
            button
        } = this.getStyles();

        return (
            <div>
                <AppBar
                    showMenuIconButton={false}
                    zDepth={0}
                />

                <div style={container}>
                    <img src="assets/img/torica.png"/>
                    <h1>{TextResources.Title}</h1>
                    <span>{TextResources.Subtitle}</span>
                    <div>
                        <RaisedButton
                            label={TextResources.button.Rental}
                            style={button}
                            onTouchTap={() => {
                                this.props.history.push("/application/lending")
                            }}/>
                        <br/>
                        <RaisedButton
                            label={TextResources.button.Return}
                            style={button}
                            onTouchTap={() => {
                                this.props.history.push("/application/return")
                            }}/>
                        <br/>
                        <RaisedButton
                            label={TextResources.button.List}
                            style={button}
                            onTouchTap={() => {
                                this.props.history.push("/list")
                            }}/>
                        <br/>
                        <RaisedButton
                            label={TextResources.button.Join}
                            style={button}
                            onTouchTap={() => {
                                this.props.history.push("/join")
                            }}/>
                        <br/>
                        <RaisedButton
                            label={TextResources.button.Manage}
                            style={button}
                        />
                    </div>
                </div>
            </div>
        )
    }
}
