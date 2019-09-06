<template>
  <div>
    <b-spinner v-if="loading">
      <span class="sr-only">Loading..</span>
    </b-spinner>
    <b-container v-else>
      <b-row>
        <b-col
          align-self="center"
          offset="3"
          cols="6"
        >
          <top-card
            :externalLink="`spotify:artist:${artist.id}`"
            :src="artist.imageUrl"
            :alt="`${artist.name}'s profile picture`"
            :footer="artist.name"
          ></top-card>
        </b-col>
      </b-row>

      <b-row 
        class="mt-1"
      >
        <b-col
          align-self="center"
        >
          <b-button
            v-for="(genre, index) in artist.genres"
            :key="index"
            :to="{ path: '/search/artist', query: { genres: genre}}"
            class="mx-1 my-1"
            pill
          >{{ genre }}</b-button>
        </b-col>
      </b-row>

      <b-row>
        <b-col
          v-for="(album,index) in albums"
          :key="index"
          cols="3"
          class="my-2"
        >
          <top-card
            :internalLink="`/albums/${album._id}`"
            :externalLink="`spotify:album:${album._id}`"
            :src="album.image"
            :alt="`${album.title}'s album art`"
            :footer="album.title"
          ></top-card>
        </b-col>
      </b-row>
    </b-container>
  </div> 
</template>

<script>
import TopCard from '@/components/common/TopCard';

export default {
  name: 'ArtistPage',
  components: {
    TopCard
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
  props: {
    artistID: {
        type: String,
        required: true
      }
  }
}
</script>
