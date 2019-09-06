<template>
    <b-container>
      <b-tabs>
        <artist-search-tab/>
        <album-search-tab/>
      </b-tabs>
    </b-container>
</template>

<script>
import ArtistSearchTab from '@/components/pages/search/ArtistSearchTab';
import AlbumSearchTab from '@/components/pages/search/AlbumSearchTab';

export default {
  name: 'SearchPage',
  components: {
    ArtistSearchTab,
    AlbumSearchTab
  }
}
</script>
