const axios = require('axios');

const api = axios.create({
  baseURL: 'https://hiretrack-backend-f2g1.onrender.com/api'
});

console.log('Resulting URL:', api.getUri({ url: '/analytics/admin' }));
console.log('Resulting URL without leading slash:', api.getUri({ url: 'analytics/admin' }));
