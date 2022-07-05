import ProductItem from "./ProductItem";
import classes from "../list.module.css";
import { Link, Route } from "react-router-dom";
import { Switch } from "react-router-dom";


const DUMMY_DATA = [
  {
    id: 0,
    name: "Sombrero",
    price: 34.70,
    description: "This is a good sombrero!",
    qunatity: 3,
    category: "Men hats"
  },
  {
    id: 1,
    name: "Another Sombrero",
    price: 34.90,
    description: "This is a better sombrero!",
    qunatity: 2,
    category: "Men better hats"
  },
];


const ProductListPage = (props) => {
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
      <h2 className= {classes.content}>
       Products in shop:
        </h2>
        {DUMMY_DATA.map((product) => (
          <ProductItem
            key={product.id}
            id={product.id}
            shopId= {props.shopId}
            name= {product.name}
            price={product.price}
            category={product.category}
            qunatity={product.qunatity}
            description={product.description}>
           </ProductItem>
        ))}
      </ul>
    </>
  );
};

function ProductList(props) {
  return (
    <>
      <Switch>
        <Route path={`/all-shops/${props.shopId}`} exact><ProductListPage shopId={props.shopId}/></Route> 
      </Switch>
    </>
  );
}
export default ProductList;
