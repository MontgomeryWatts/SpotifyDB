<template>
  <b-card
    no-body
  >
    <b-link
      :href="externalLink"
    >
      <b-card-img
        :src="src"
        :alt="alt"
        top
      ></b-card-img>
    </b-link>

    <b-link
      v-if="internalLink"
      :to="internalLink"
    >
      {{ footer }}
    </b-link>

    <b-card-footer
      v-else
    >
      {{ footer }}
    </b-card-footer>

  </b-card>
</template>

<script>
export default {
  name: 'TopCard',
  props: {
    footer: {
      type: String,
      required: true
    },
    internalLink: {
      type: String,
      required: false
    },
    externalLink: {
      type: String,
      required: true
    },
    src: {
      type: String,
      required: true
    },
    alt: {
      type: String,
      required: false
    }
  }
}
</script>