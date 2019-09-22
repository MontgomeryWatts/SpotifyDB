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
    </b-container>
  </div> 
</template>

<script>
import service from '@/services/artist-service';
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
    HorizontalCard
  },
  data () {
    return {
      artist: {
        id: '',
        name: '',
        imageUrl: '',
        genres: []
      },
      albums: [],
      loading: true
    }
  },
  mounted () {
    this.getArtist();
  },
  methods: {
    async getArtist () {
      try {
        let response = await service.getArtistById(this.artistId);
        this.artist = response.data;
        this.loading = false;
      } catch (e) {
        throw e;
      }
    }
  }
}
</script>
