<template>
  <horizontal-card
    :imageSrc="album.images[0].url"
    :title="album.title"
  />
</template>

<script>
import HorizontalCard from '@/components/common/HorizontalCard'

export default {
  name: 'ArtistAlbum',
  components: {
    HorizontalCard
  },
  props: {
    album: {
      required: true,
      type: Object
    }
  }
};
</script>