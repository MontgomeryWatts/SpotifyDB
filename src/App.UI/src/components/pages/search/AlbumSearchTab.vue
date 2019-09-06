<template>
  <b-tab title="Albums">
    <b-form>
      <b-form-group
        class="mt-3"
        id="album-title-group"
        label-cols="2"
        label-align="right"
        label="Title"
        label-for="album-title"
      >
        <b-form-input
          id="album-title"
          placeholder="Enter album title"
        >
        </b-form-input>
      </b-form-group>
    </b-form>
  </b-tab>
</template>

<script>
export default {
  name: 'AlbumSearchTab'
}
</script>