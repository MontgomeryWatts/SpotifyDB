import axios from 'axios';

export default {
  getAlbumById (id) {
    return axios({
      method: 'get',
      url: `/api/albums/${id}`
    })
  }
}