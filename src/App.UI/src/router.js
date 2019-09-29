import Vue from 'vue';
import VueRouter from 'vue-router';
import artistService from '@/services/artist-service';

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
      path: '/artists/random', 
      beforeEnter: async (to, from, next) => { 
        var response = await artistService.getRandomArtistId();
        var id = response.data;
        next(`/artists/${id}`);
      }
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
    },
    {
      path: '/404',
      component: () => import(/* webpackChunkName: "404" */ '@/components/pages/error/NotFound')
    },
    {
      path: '*',
      component: () => import(/* webpackChunkName: "404" */ '@/components/pages/error/NotFound')
    }
  ]
});

export default router;