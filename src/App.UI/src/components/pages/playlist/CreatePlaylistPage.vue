<template>
  <b-container>
    <b-card> 
      <b-card-body>
        <b-form>
          <b-input-group>
            <b-form-input placeholder="Artist Name" v-model="name"></b-form-input>
            <b-input-group-append>
              <b-button 
                @click="addName"
                :disabled="name === ''"
              >Add</b-button>
            </b-input-group-append>

          </b-input-group>

          <b-button
            @click="removeName(name)"
            v-for="(name, index) in names"
            :key="index"
            size="sm"
            class="mx-1 mt-2"
            pill
          >{{ name }}</b-button>

          <b-input-group class="mt-3">
            <b-form-input placeholder="Genre" v-model="genre "></b-form-input>
            <b-input-group-append>
              <b-button 
                @click="addGenre"
                :disabled="genre === ''"
              >Add</b-button>
            </b-input-group-append>
          </b-input-group>

          <b-button
            @click="removeGenre(genre)"
            v-for="(genre, index) in genres"
            :key="index"
            size="sm"
            class="mx-1 mt-2"
            pill
          >{{ genre }}</b-button>

          <b-input-group
            prepend="Duration"
            :append="durationString"
            class="mt-3"
          >
            <b-form-input type="range" min="15" max="120" v-model="duration"></b-form-input>
          </b-input-group>

          <b-form-checkbox 
            v-model="allowExplicit"
            class="mt-3 float-left"
            switch
          >
            Allow Explicit
          </b-form-checkbox>
        </b-form>
      </b-card-body>
    </b-card>
  </b-container>
</template>

<script>
export default {
  name: 'CreatePlaylistPage',
  data () {
    return {
      name: '',
      genre: '',
      names: [],
      genres: [],
      duration: 60,
      allowExplicit: false
    }
  },
  methods: {
    addName() {
      this.names.push(this.name);
      this.name = '';
    },
    addGenre() {
      this.genres.push(this.genre);
      this.genre = '';
    },
    removeName(target) {
      this.names = this.names.filter(name => name !== target);
    },
    removeGenre(target) {
      this.genres = this.genres.filter(genre => genre !== target);
    }
  },
  computed : {
    durationString: function () {
      return Math.floor(this.duration / 60) + ":" + (this.duration % 60).toString().padStart(2, '0');
    }
  }
}
</script>
