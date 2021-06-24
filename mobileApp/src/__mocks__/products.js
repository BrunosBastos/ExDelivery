import { v4 as uuid } from 'uuid';

export default [
  {
    id: uuid(),
    supplier: 'Farmacia 1',
    description: 'Dropbox is a file hosting service that offers cloud storage, file synchronization, a personal cloud.',
    image: '/static/images/products/product_1.png',
    name: 'Dropbox',
    price: 25.42,
    quantity: 1
  },
  {
    id: uuid(),
    supplier: 'Farmacia 2',
    description: 'Medium is an online publishing platform developed by Evan Williams, and launched in August 2012.',
    image: '/static/images/products/product_2.png',
    name: 'Medium Corporation',
    price: 30.99,
    quantity: 1
  },
  {
    id: uuid(),
    supplier: 'Farmacia 3',
    description: 'Slack is a cloud-based set of team collaboration tools and services, founded by Stewart Butterfield.',
    image: '/static/images/products/product_3.png',
    name: 'Slack',
    price: 19.99,
    quantity: 1
  },
  {
    id: uuid(),
    supplier: 'Farmacia 4',
    description: 'Lyft is an on-demand transportation company based in San Francisco, California.',
    image: '/static/images/products/product_4.png',
    name: 'Lyft',
    price: 9.99,
    quantity: 1
  },
  {
    id: uuid(),
    supplier: 'Farmacia 5',
    description: 'GitHub is a web-based hosting service for version control of code using Git.',
    image: '/static/images/products/product_5.png',
    name: 'GitHub',
    price: 2.5,
    quantity: 1
  },
  {
    id: uuid(),
    supplier: 'Farmacia 6',
    description: 'Squarespace provides software as a service for website building and hosting. Headquartered in NYC.',
    image: '/static/images/products/product_6.png',
    name: 'Squarespace',
    price: 51.99,
    quantity: 1
  }
];
