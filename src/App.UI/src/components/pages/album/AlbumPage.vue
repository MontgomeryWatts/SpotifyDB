<template>
  <div>
    <b-spinner v-if="loading">
      <span class="sr-only">Loading..</span>
    </b-spinner>
    <b-container v-else>
      <horizontal-card
        :title="album.title"
        :imageSrc="album.images.length > 0 ? album.images[0].url : ''"
      >
      </horizontal-card>
      <b-list-group>
        <b-list-group-item v-for="(track, index) in album.tracks" :key="track.uri">
          <span class="float-left">{{ index + 1 }}</span>
          <b-link :href="track.uri">
            {{ track.title }}
          </b-link>
          <span class="float-right">{{ track.duration | durationString }} </span>
        </b-list-group-item>
      </b-list-group>
    </b-container>
  </div> 
</template>

<script>
import HorizontalCard from '@/components/common/HorizontalCard';

import service from '@/services/album-service';

export default {
  name: 'AlbumPage',
  components: {
    HorizontalCard
  },
  props: {
    albumId: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      album: null,
      loading: true
    }
  },
  mounted () {
    this.loadPage(this.albumId);
  },
  async beforeRouteUpdate (to, from, next) {
    await this.loadPage(to.params.albumId);
    next();
  },
  methods: {
    async loadPage (albumId) {
      this.loading = true;
      this.album = null;
      await this.getAlbum(albumId);
    },
    async getAlbum (albumId) {
      try {
        let response = await service.getAlbumById(albumId);
        this.album = response.data;
      } catch (e) {
        if (e.response.status === 404) {
          this.$router.push('/404');
        }
      } finally {
        this.loading = false;
      }
    }
  },
  filters: {
    durationString (duration) {
      return Math.floor(duration / 60) + ":" + (duration % 60).toString().padStart(2, '0');
    }
  }
}
</script>
