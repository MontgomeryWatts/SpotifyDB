import axios from 'axios';

export default {
  getArtistById (id) {
    return axios({
      method: 'get',
      url: `/api/artists/${id}`
    })
  },
  getArtistAlbumsById (id) {
    return axios({
      method: 'get',
      url: `/api/artists/${id}/albums`
    })
  }
}