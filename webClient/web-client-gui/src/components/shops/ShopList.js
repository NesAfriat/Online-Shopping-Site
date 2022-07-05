import ShopItem from "./ShopItem";
import classes from "../list.module.css";
import { Link, Route } from "react-router-dom";
import { Switch } from "react-router-dom";
import AddNewProduct from "../Forms/AddNewProduct";
import AddProductDiscountForm from "../Forms/AddProductDiscount";
import ProductList from "../products/ProductList"

const DUMMY_DATA = [
  {
    id: 0,
    founder: "Grandpa Rick",
    name: "peace among worlds",
    location: "Fruppi land",
    description:
      "This is a first, amazing shop which you definitely should not miss. Only socks!",
  },
  {
    id: 1,
    founder: "Admin",
    name: "Noobs-only",
    location: "Meetupstreet 5, 12345 Meetup City",
    description: "My first shop, be gentle",
  },
];
const ShopPage = ({ match }) => {
  const {
    params: { shopId },
  } = match;
  return (
    <>
      <h1>{DUMMY_DATA[shopId].name} - Manager's page</h1>
      <h3>Founder: {DUMMY_DATA[shopId].founder}</h3>
      <h3>Location: {DUMMY_DATA[shopId].location}</h3>
      <h3>description: {DUMMY_DATA[shopId].description}</h3>
      <div className={classes.action}>
      <ProductList shopId= {shopId}/>
        <nav>
          <ul>
            <li>
              <Link to={`/all-shops/${shopId}/add-new-product`}>
                Add new product
              </Link>
            </li>
            <li>
              <Link to={`/all-shops/${shopId}/create-new-product-discount`}>
                Create a product discount
              </Link>
            </li>
            <li>
              <Link to="/all-shops/">Back to shops lists</Link>
            </li>
          </ul>
        </nav>
      </div>
      <Switch>
        <Route path={`/all-shops/${shopId}/add-new-product`}>
          <AddNewProduct shopId={shopId} />
        </Route>
        <Route path={`/all-shops/${shopId}/create-new-product-discount`}>
          <AddProductDiscountForm shopId={shopId} />
        </Route>
        <Route path="/all-shops" exact component={ShopListPage} />
      </Switch>
    </>
  );
};

const ShopListPage = () => {
  // const shops = await getAllOpenShops(visitorId.id).then((res) => {
  //   if (res.value.length !== 0) {
  //     return JSON.parse(res.value);
  //   } else {
  //     return DUMMY_DATA;
  //   }
  // });
  // console.log(shops);

  return (
    <>
      <ul className={classes.list}>
        {DUMMY_DATA.map((shop) => (
          <ShopItem
            key={shop.id}
            id={shop.id}
            name={shop.name}
            founder={shop.founder}
            location={shop.location}
            description={shop.description}
          />
        ))}
      </ul>
    </>
  );
};

function ShopList() {
  return (
    <>
      <Switch>
        <Route path="/all-shops" exact component={ShopListPage} />
        <Route path="/all-shops/:shopId" component={ShopPage} />
      </Switch>
    </>
  );
}
export default ShopList;
