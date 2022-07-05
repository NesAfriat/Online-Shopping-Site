import classes from "../styling/list.module.css";
import GeneralProduct from "./BasicComponent/GeneralProduct";

const ViewAllProducts = (props) => {
    return (
        <>
            <h2 className={classes.content}>Products: </h2>
            <ul className={classes.list}>
                {Object.entries(props.products).map(([productId, product], i) => {
                    return (
                        <li key={i}>
                            <GeneralProduct
                                visitorId={props.visitorId}
                                id={product.id}
                                shopId={props.shopId}
                                name={product.productName}
                                price={product.price}
                                category={product.category}
                                quantity={product.quantity}
                                description={product.description}
                                member={props.member}
                            />
                        </li>
                    );
                })}
            </ul>
        </>
    );
};

export default ViewAllProducts;
