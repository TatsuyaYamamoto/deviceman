import React from 'react'
import PropTypes from 'prop-types'

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

const SuccessJoinDialog = (props) => {
    const {open, handleCloseDialog} = props;

    return (
        <Dialog
            title="Thank you for joining!"
            open={open}
            modal={true}
            actions={[
                <FlatButton
                    label="OK"
                    primary={true}
                    onTouchTap={handleCloseDialog}
                />
            ]}>
            The actions in this window were passed in as an array of React objects.
        </Dialog>
    )
};

SuccessJoinDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    handleCloseDialog: PropTypes.func,
};

export default SuccessJoinDialog;
