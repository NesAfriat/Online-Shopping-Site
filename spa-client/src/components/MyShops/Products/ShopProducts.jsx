import {useState} from "react";
import classes from "../../../styling/list2.module.css";
import LoadProductsShop from "./LoadProductsShop";
import AddProduct from "./Actions/AddProduct";
import UpdateProductName from "./Actions/UpdateProductName";
import UpdateProductDesc from "./Actions/UpdateProductDesc";
import UpdateProductPrice from "./Actions/UpdateProductPrice";
import UpdateProductAmount from "./Actions/UpdateProductAmount";

const ShopProducts = (props) => {
    const [showShopProducts, setShowShopProducts] = useState(false);
    const [showAddProduct, setShowAddProduct] = useState(false);
    const [showUpdateName, setShowUpdateName] = useState(false);
    const [showUpdateDesc, setShowUpdateDesc] = useState(false);
    const [showUpdatePrice, setShowUpdatePrice] = useState(false);
    const [showUpdateAmount, setShowUpdateAmount] = useState(false);

    const viewShopProductsHandler = (e) => {
        e.preventDefault();
        setShowShopProducts(true);
        setShowAddProduct(false);
        setShowUpdateName(false);
        setShowUpdateDesc(false);
        setShowUpdatePrice(false);
        setShowUpdateAmount(false);
    };

    const viewAddProduct = (e) => {
        e.preventDefault();
        setShowShopProducts(false);
        setShowAddProduct(true);
        setShowUpdateName(false);
        setShowUpdateDesc(false);
        setShowUpdatePrice(false);
        setShowUpdateAmount(false);
    };

    const viewUpdateName = (e) => {
        e.preventDefault();
        setShowShopProducts(false);
        setShowAddProduct(false);
        setShowUpdateName(true);
        setShowUpdateDesc(false);
        setShowUpdatePrice(false);
        setShowUpdateAmount(false);
    };

    const viewUpdateDescription = (e) => {
        e.preventDefault();
        setShowShopProducts(false);
        setShowAddProduct(false);
        setShowUpdateName(false);
        setShowUpdateDesc(true);
        setShowUpdatePrice(false);
        setShowUpdateAmount(false);
    };

    const viewUpdatePrice = (e) => {
        e.preventDefault();
        setShowShopProducts(false);
        setShowAddProduct(false);
        setShowUpdateName(false);
        setShowUpdateDesc(false);
        setShowUpdatePrice(true);
        setShowUpdateAmount(false);
    };

    const viewUpdateAmount = (e) => {
        e.preventDefault();
        setShowShopProducts(false);
        setShowAddProduct(false);
        setShowUpdateName(false);
        setShowUpdateDesc(false);
        setShowUpdatePrice(false);
        setShowUpdateAmount(true);
    };

    return (
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.list}>
                        <li>
                            <button onClick={viewShopProductsHandler}>
                                <h4>Shop products</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewAddProduct}>
                                <h4>Add product</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewUpdateName}>
                                <h4>Update product name</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewUpdateDescription}>
                                <h4>Update product desc</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewUpdatePrice}>
                                <h4>Update product price</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewUpdateAmount}>
                                <h4>Update product amount</h4>
                            </button>
                        </li>
                    </ul>
                </div>
            </header>
            <div>
                {showShopProducts && (
                    <LoadProductsShop
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        products={props.products}
                        setter={props.setter}
                    />
                )}
                {showAddProduct && (
                    <AddProduct
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setShowAddProduct}
                        reload={props.setter}
                    />
                )}
                {showUpdateName && (
                    <UpdateProductName
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setShowUpdateName}
                        reload={props.setter}
                    />
                )}
                {showUpdateDesc && (
                    <UpdateProductDesc
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setShowUpdateDesc}
                        reload={props.setter}
                    />
                )}
                {showUpdatePrice && (
                    <UpdateProductPrice
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setShowUpdatePrice}
                        reload={props.setter}
                    />
                )}
                {showUpdateAmount && (
                    <UpdateProductAmount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setShowUpdateAmount}
                        reload={props.setter}
                    />
                )}
            </div>
        </div>
    );
};

export default ShopProducts;
