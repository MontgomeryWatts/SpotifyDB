<template>
  <b-container>
    <b-row>
      <b-col v-for="(genre,index) in genres" :key="index" sm="3">
        <b-link :to="{ path: '/search/artist', query: { genres: genre}}">{{ genre }}</b-link>
      </b-col>
    </b-row>
  </b-container> 
</template>

<script>
export default {
  name: 'GenrePage', 
  data () {
    return {
      genres: []
    }
  }
}
</script>
