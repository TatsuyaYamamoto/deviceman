import React from 'react'
import {HashRouter as Router, Switch, Route, Redirect} from 'react-router-dom';

import {ThemeComponent} from './Theme'
import TopContainer from './views/top/TopView'

export default function () {
    return (
        <ThemeComponent>
            <Router>
                <Switch>
                    <Route exact path="/" component={TopContainer}/>
                    <Redirect from="*" to="/"/>
                </Switch>
            </Router>
        </ThemeComponent>
    )
}
