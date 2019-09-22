<template>
  <div>
    <b-spinner v-if="loading">
      <span class="sr-only">Loading..</span>
    </b-spinner>
    <b-container v-else>
      <horizontal-card
        :title="artist.name"
        :imageSrc="artist.images[0].url"
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
          cols="4"
          class="mt-2"
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
    this.loadPage();
  },
  async beforeRouteUpdate (to, from, next) {
    this.artistId = to.params.artistId;
    await this.loadPage();
    next();
  },
  methods: {
    async loadPage () {
      this.loading = true;
      this.artist = null;
      this.albums = [];
      await this.getArtist();
      if (this.artist) await this.getArtistAlbums();
    },
    async getArtist () {
      try {
        let response = await service.getArtistById(this.artistId);
        this.artist = response.data;
        this.loading = false;
      } catch (e) {
        throw e;
      }
    },
    async getArtistAlbums () {
      try {
        let response = await service.getArtistAlbumsById(this.artistId);
        this.albums = response.data;
      } catch (e) {
        throw e;
      }
    }
  }
}
</script>
