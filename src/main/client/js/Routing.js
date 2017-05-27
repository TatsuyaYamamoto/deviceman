import React from 'react'
import {HashRouter as Router, Switch, Route, Redirect} from 'react-router-dom';

import {ThemeComponent} from './Theme'
import Top from './views/top'
import Join from './views/join'

export default function () {
    return (
        <ThemeComponent>
            <Router>
                <Switch>
                    <Route exact path="/" component={Top}/>
                    <Route exact path="/join" component={Join}/>
                    <Redirect from="*" to="/"/>
                </Switch>
            </Router>
        </ThemeComponent>
    )
}
