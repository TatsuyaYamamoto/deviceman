import React from 'react'
import {render} from 'react-dom'
import injectTapEventPlugin from 'react-tap-event-plugin';

import Routing from './Routing';

injectTapEventPlugin();

render(
    <Routing />,
    document.getElementById('content')
);
