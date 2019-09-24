<template>
  <top-card
    :src="album.images[0].url"
    :footer="album.title"
  />
</template>

<script>
import TopCard from '@/components/common/TopCard'

export default {
  name: 'ArtistAlbum',
  components: {
    TopCard
  },
  props: {
    album: {
      required: true,
      type: Object
    }
  }
};
</script>