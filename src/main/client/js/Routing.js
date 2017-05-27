import React from 'react'
import {HashRouter as Router, Switch, Route, Redirect} from 'react-router-dom';

import {ThemeComponent} from './Theme'
import Top from './views/top'
import Join from './views/join'
import LendingApplication from './views/application/lending'
import ReturnApplication from './views/application/return'

export default function () {
    return (
        <ThemeComponent>
            <Router>
                <Switch>
                    <Route exact path="/" component={Top}/>
                    <Route exact path="/join" component={Join}/>
                    <Route exact path="/application/lending" component={LendingApplication}/>
                    <Route exact path="/application/return" component={ReturnApplication}/>
                    <Redirect from="*" to="/"/>
                </Switch>
            </Router>
        </ThemeComponent>
    )
}
