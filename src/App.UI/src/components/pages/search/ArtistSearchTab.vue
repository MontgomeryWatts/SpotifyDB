<template>
  <b-tab title="Artists" active>
    <b-form>
      <b-form-group
        class="mt-3"
        id="artist-name-group"
        label-cols="2"
        label-align="right"
        label="Name"
        label-for="artist-name"
      >
        <b-form-input
          id="artist-name"
          placeholder="Enter artist name"
        >
        </b-form-input>
      </b-form-group>

      <b-form-group
        label-cols="2"
        label-align="right"
        label="Genres"
        label-for="genres"
      >
        <b-form-input
          id="genres"
          placeholder="Enter genre to add"
        >
        </b-form-input>
      </b-form-group>

      <b-form-group
        label-cols="2"
        label-align="right"
        label="Genre Inclusion"
        label-for="genre-options"
      >
        <b-form-radio-group
          id="genre-options"
          v-model="selected"
          :options="genresOptions"
        >
        </b-form-radio-group>
      </b-form-group>
    </b-form>
  </b-tab>
</template>

<script>
export default {
  name: 'ArtistSearchTab',
  data () {
    return {
      selected: 'AND',
      genresOptions: [
        { value: 'AND', text:'All'},
        { value: 'OR', text:'Any'},
        { value: 'NOT', text:'Exclude'}
      ],
      genres : []
    }
  }
}
</script>