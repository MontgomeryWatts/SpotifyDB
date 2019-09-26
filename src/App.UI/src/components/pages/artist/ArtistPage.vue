<template>
  <div>
    <b-spinner v-if="loading">
      <span class="sr-only">Loading..</span>
    </b-spinner>
    <b-container v-else>
      <horizontal-card
        :title="artist.name"
        :imageSrc="artist.images.length > 0 ? artist.images[0].url : ''"
      >
        <template v-slot:content>
         <b-badge
           pill
           class="mx-1"
           v-for="genre in artist.genres"
           :key="genre"
          >{{ genre }}</b-badge>
        </template>
      </horizontal-card>
      <b-row>
        <b-col 
          xs="12"
          md="3"
          class="my-2"
          v-for="album in albums"
          :key="album.id"
        >
          <artist-album :album="album"/>
        </b-col>
      </b-row>
    </b-container>
  </div> 
</template>

<script>
import service from '@/services/artist-service';
import ArtistAlbum from '@/components/pages/artist/ArtistAlbum';
import HorizontalCard from '@/components/common/HorizontalCard';

export default {
  name: 'ArtistPage',
  props: {
    artistId: {
        type: String,
        required: true
      }
  },
  components: {
    ArtistAlbum,
    HorizontalCard
  },
  data () {
    return {
      artist: null,
      albums: [],
      loading: true
    }
  },
  mounted () {
    this.loadPage(this.artistId);
  },
  async beforeRouteUpdate (to, from, next) {
    await this.loadPage(to.params.artistId);
    next();
  },
  methods: {
    async loadPage (artistId) {
      this.loading = true;
      this.artist = null;
      this.albums = [];
      await this.getArtist(artistId);
      if (this.artist) await this.getArtistAlbums(artistId);
    },
    async getArtist (artistId) {
      try {
        let response = await service.getArtistById(artistId);
        this.artist = response.data;
      } catch (e) {
        throw e;
      } finally {
        this.loading = false;
      }
    },
    async getArtistAlbums (artistId) {
      try {
        let response = await service.getArtistAlbumsById(artistId);
        this.albums = response.data;
      } catch (e) {
        throw e;
      }
    }
  }
}
</script>
