<template>
  <div>
    <b-spinner v-if="loading"></b-spinner>
    <b-container v-else>
      <b-row>
        <b-col
          align-self="center"
          offset="3"
          cols="6"
        >
          <top-card
            :externalLink="`spotify:album:${album.id}`"
            :src="album.imageUrl"
            :alt="`${album.title}'s album art`"
            :footer="album.title"
          ></top-card>
        </b-col>
      </b-row>

      <b-link 
        v-for="(credit) in album.credits" 
        :key="credit.id"
        :to="`/artists/${credit.id}`"
      >
        {{ credit.name }} 
      </b-link>
      
      <b-list-group>
        <b-list-group-item 
          v-for="(song, index) in album.songs" 
          :key="index"
          :href="`spotify:track:${song.trackId}`"
        >
          {{ song.title }}
        </b-list-group-item>
      </b-list-group>
    </b-container>
  </div>
</template>

<script>
import TopCard from '@/components/common/TopCard';

export default {
  name: 'AlbumPage',
  components: {
    TopCard
  },
  data () {
    return {
      album: {
        id: '',
        title: '',
        imageUrl: '',
        credits: [],
        songs: []
      },
      loading: true
    }
  },
  props: {
    albumId: {
      type: String,
      required: true
    }
  }
}
</script>
