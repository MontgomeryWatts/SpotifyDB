<template>
  <b-navbar role="navigation" toggleable="sm" fixed="top" variant="dark" type="dark">
    <b-navbar-brand to="/" aria-label="Go to home page">SpotifyDB</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item to="/playlist">Create Playlist</b-nav-item>

        <b-nav-item-dropdown text="Discover">
          <b-dropdown-item to="/artists/random">Random Artist</b-dropdown-item>
          <b-dropdown-item to="/genres">Genres</b-dropdown-item>
          <b-dropdown-item to="/search">Search</b-dropdown-item>   
        </b-nav-item-dropdown>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-form role="search">
          <b-input-group>
            <b-form-input v-model="query" placeholder="Search Artists" aria-label="Search artists by name"></b-form-input>
            <b-button type="submit" slot="append" aria-label="Start artist search">
              <v-icon name="search"></v-icon>
            </b-button>
          </b-input-group>
        </b-nav-form>

        <b-nav-item class="ml-2" role="button" aria-label="Open settings menu">
          <v-icon name="cog"></v-icon>
        </b-nav-item>

      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import VIcon from 'vue-awesome/components/Icon';
import 'vue-awesome/icons/cog';
import 'vue-awesome/icons/search';

export default {
    name: 'Navbar',
    components: {
      VIcon
    },
    data: function() {
      return {
        query: ''
      }
    }
}
</script>
