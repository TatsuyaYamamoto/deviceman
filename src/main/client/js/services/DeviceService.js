import axios from 'axios';

export const SUCCESS = 'success';
export const ERROR = 'error';
export const ERROR_DEVICE_ON_LENDING = 'error_device_on_lending';
export const ERROR_INVALID_DUE_RETURN_DATE = 'error_invalid_due_return_date';
export const ERROR_DEVICE_NOT_ON_LENDING = 'error_device_not_on_lending';

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

export function applyLending(deviceId, userId, dueReturnDate) {
    return axios
        .put(`/api/devices/${deviceId}/lending/`, {
            userId: userId,
            dueReturnDate: dueReturnDate
        })
        .then(function (response) {
            return SUCCESS;
        })
        .catch(function (error) {
            if (error.response.status == 409) {
                return ERROR_DEVICE_ON_LENDING;
            } else if (error.response.status == 400) {
                return ERROR_INVALID_DUE_RETURN_DATE;
            } else {
                return ERROR;
            }
        })
}

export function applyReturn(deviceId) {
    return axios
        .delete(`/api/devices/${deviceId}/lending/`)
        .then(function (response) {
            return SUCCESS;
        })
        .catch(function (error) {
            if (error.response.status == 404) {
                return ERROR_DEVICE_NOT_ON_LENDING;
            } else {
                return ERROR;
            }
        })
}
