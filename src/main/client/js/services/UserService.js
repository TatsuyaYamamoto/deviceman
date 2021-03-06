import axios from 'axios';

export const SUCCESS = 'success';
export const ERROR = 'error';
export const ERROR_CONFLICT = 'error_conflict';

export function search(query) {
    return axios
        .get('/api/users/', {
            params: {
                query: query
            }
        })
        .then(function (response) {
            return response.data.users;
        })
}

export function createUser(id, address) {
    return axios
        .post('/api/users/', {
            id: id,
            address: address
        })
        .then(function (response) {
            return SUCCESS;
        })
        .catch(function (error) {
            if (error.response.status == 409) {
                return ERROR_CONFLICT;
            } else {
                return ERROR;
            }
        })
}
