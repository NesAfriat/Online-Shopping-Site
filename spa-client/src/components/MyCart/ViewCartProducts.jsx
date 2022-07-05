import classes from "../../styling/list.module.css";
import CartProduct from "../BasicComponent/CartProduct";

const ViewCartProducts = (props) => {


    console.log("rendering cart prods");
    return (
        //render the basktes to somehow data
        <>
            <h1 className={classes.content}>My products: </h1>
            {props.cart !== null &&
                Object.entries(props.cart.baskets).length > 0 &&
                Object.entries(props.cart.baskets).map(([shopId, basket]) => {
                    return (<ul className={classes.list}><h2>Shop ID: {shopId}</h2>  {
                        // eslint-disable-next-line array-callback-return
                        Object.entries(basket.products).map(([productId, pair]) => {
                            return (
                                <li key={productId}>
                                    <CartProduct
                                        visitorId={props.visitorId}
                                        id={productId}
                                        shopId={shopId}
                                        name={pair.value.productName}
                                        price={pair.value.price}
                                        category={pair.value.category}
                                        qunatity={pair.key}
                                        description={pair.value.description}
                                        reloadCart={props.reloadCart}
                                    ></CartProduct>
                                </li>
                            );
                        })}</ul>)
                })}</>);
};

export default ViewCartProducts;
