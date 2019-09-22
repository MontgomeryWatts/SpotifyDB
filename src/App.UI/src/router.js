import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: () => import(/* webpackChunkName: "HomePage" */ '@/components/pages/home/HomePage')
    },
    {
      path: '/search',
      component: () => import(/* webpackChunkName: "SearchPage" */ '@/components/pages/search/SearchPage')
    },
    {
      path: '/artists/:artistId',
      component: () => import(/* webpackChunkName: "ArtistPage" */ '@/components/pages/artist/ArtistPage'),
      props: true
    },
    {
      path: '/albums/:albumId',
      component: () => import(/* webpackChunkName: "AlbumPage" */ '@/components/pages/album/AlbumPage'),
      props: true
    },
    {
      path: '/genres',
      component: () => import(/* webpackChunkName: "GenrePage" */ '@/components/pages/genre/GenrePage')
    },
    {
      path: '/playlist',
      component: () => import(/* webpackChunkName: "CreatePlaylistPage" */ '@/components/pages/playlist/CreatePlaylistPage')
    }
  ]
});

export default router;