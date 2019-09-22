<template>
  <b-card no-body class="overflow-hidden">
    <b-row no-gutters>
      <b-col md="6">
        <b-card-img :src="imageSrc" class="rounded-0"></b-card-img>
      </b-col>
      <b-col md="6">
        <b-card-title class="border-bottom py-2">{{ title }}</b-card-title>
        <b-card-body>
          <slot name="content"></slot>
        </b-card-body>
      </b-col>
    </b-row>
  </b-card>
</template>

<script>
export default {
  name: 'HorizontalCard',
  props: {
    imageSrc: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: true
    }
  }
};
</script>