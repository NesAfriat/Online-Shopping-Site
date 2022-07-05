import classes from "../../../styling/list.module.css";
import {useState} from "react";
import Card from "../../../styling/Card";
import classes2 from "../../../styling/item.module.css";
import MyShopProduct from "../../BasicComponent/MyShopProduct";

const LoadProductsShop = (props) => {

    const [showProduct, setShowProduct] = useState(null);

    const updateProduct = (product) => {
        setShowProduct(product);
    }

    return (
        <>
            {props.products !== null && <h3>No products found</h3>}
            <ul className={classes.list}>
                {Object.entries(props.products).map(([productId, product], i) => {
                        return (
                            <li key={i}>
                                <button className={classes.actions} onClick={() => updateProduct(product)}>
                                    <Card>
                                        <div className={classes2.content}>
                                            <h1>{product.productName}</h1>
                                            <h2>id:{product.id}</h2>
                                        </div>
                                    </Card>
                                </button>
                            </li>
                        )
                    }
                )}
            </ul>
            {showProduct && (
                <>
                    <MyShopProduct
                        visitorId={props.visitorId}
                        id={showProduct.id}
                        shopId={props.shopId}
                        name={showProduct.productName}
                        price={showProduct.price}
                        category={showProduct.category}
                        qunatity={showProduct.quantity}
                        description={showProduct.description}
                        setter={props.setter}>
                    </MyShopProduct>
                </>

            )}

        </>
    )
};

export default LoadProductsShop;