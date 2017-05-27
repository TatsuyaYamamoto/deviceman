import React from 'react'
import {HashRouter as Router, Switch, Route, Redirect} from 'react-router-dom';

import {ThemeComponent} from './Theme'
import Top from './views/top'
import Join from './views/join'
import LendingApplication from './views/application/lending'

export default function () {
    return (
        <ThemeComponent>
            <Router>
                <Switch>
                    <Route exact path="/" component={Top}/>
                    <Route exact path="/join" component={Join}/>
                    <Route exact path="/application/lending" component={LendingApplication}/>
                    <Redirect from="*" to="/"/>
                </Switch>
            </Router>
        </ThemeComponent>
    )
}
