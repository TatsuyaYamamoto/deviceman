import React from 'react'

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import * as Colors from 'material-ui/styles/colors';
import * as ColorManipulator from 'material-ui/utils/colorManipulator'
import * as Spacing from 'material-ui/styles/spacing';
import getMuiTheme from 'material-ui/styles/getMuiTheme';

export const palette = {
    primary1Color: Colors.cyan500,
    primary2Color: Colors.cyan700,
    primary3Color: Colors.lightBlack,
    accent1Color: Colors.pinkA200,
    accent2Color: Colors.grey100,
    accent3Color: Colors.grey500,
    textColor: Colors.darkBlack,
    alternateTextColor: Colors.white,
    canvasColor: Colors.white,
    borderColor: Colors.grey300,
    disabledColor: ColorManipulator.fade(Colors.darkBlack, 0.3)
};

export const MuiTheme = getMuiTheme({
    spacing: Spacing,
    fontFamily: 'Roboto, sans-serif',
    palette: palette
});

export const ThemeComponent = (props) => {
    const {children} = props;

    return (
        <MuiThemeProvider muiTheme={MuiTheme}>
            { children }
        </MuiThemeProvider>
    );
};
