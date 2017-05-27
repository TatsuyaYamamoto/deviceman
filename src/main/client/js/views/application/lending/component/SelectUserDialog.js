import React from 'react'
import PropTypes from 'prop-types'

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';
import {List, ListItem} from 'material-ui/List';
import PersonIcon from 'material-ui/svg-icons/social/person';

let inputTimer;
const INTERVAL = 500;
const SelectUserDialog = (props) => {
    const {
        open,
        handleSearchUser,
        handleSelectUser,
        handleCloseDialog,
        users
    } = props;

    return (
        <Dialog
            title="Select borrowing user."
            actions={[
                <FlatButton
                    label="Cancel"
                    primary={true}
                    onTouchTap={handleCloseDialog}
                />
            ]}
            modal={false}
            open={open}
            autoScrollBodyContent={true}
            onRequestClose={handleCloseDialog}>

            <TextField
                hintText="Search with ID and address."
                onChange={(event, newValue) => {
                    if (inputTimer !== false) {
                        clearTimeout(inputTimer);
                    }
                    inputTimer = setTimeout(function () {
                        handleSearchUser(newValue);
                    }, INTERVAL);
                }}
            />
            <List>
                {users.map((user, index) => {
                    return (
                        <ListItem
                            key={user.id}
                            leftIcon={<PersonIcon />}
                            primaryText={user.id}
                            secondaryText={user.address}
                            onTouchTap={() => handleSelectUser(index)}
                        />
                    );
                })}
            </List>
        </Dialog>
    )
};

SelectUserDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    handleSearchUser: PropTypes.func,
    handleSelectUser: PropTypes.func,
    handleCloseDialog: PropTypes.func,
    users: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string,
        address: PropTypes.string
    }))
};

export default SelectUserDialog;
