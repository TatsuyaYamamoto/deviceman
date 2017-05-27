import axios from 'axios';

export const SUCCESS = 'success';
export const ERROR = 'error';
export const ERROR_CONFLICT = 'error_conflict';

export function search(query) {
    return axios
        .get('/api/devices/', {
            params: {
                query: query
            }
        })
        .then(function (response) {
            return response.data.devices;
        })
}
