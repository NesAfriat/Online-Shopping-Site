import {useState} from "react";
import classes from "../../../../styling/list2.module.css";
import AddCategoryDiscount from "./AddCategoryDiscount";
import AddPQCDiscount from "./AddPQCDiscount";
import AddProductDiscount from "./AddProductDiscount";
import AddTBPDiscount from "./AddTBPDiscount";
import AddTotalShopDiscount from "./AddTotalShopDiscount";
import AddXORDiscount from "./AddXorDiscount";
import ViewAllDiscounts from "./ViewAllDiscount";
import {getDiscountPolicies} from "../../../../api";

const DiscountMenu = (props) => {
    const [viewAllD, setViewAllD] = useState(false);
    const [addProductD, setAddProductD] = useState(false);
    const [addCategoryD, setAddCategoryD] = useState(false);
    const [addShopD, setAddShopD] = useState(false);
    const [addXORD, setXORD] = useState(false);
    const [addPQCD, setPQCD] = useState(false);
    const [addTBPD, setTBPD] = useState(false);

    const [shopDiscountPolicy, setShopDiscountPolicy] = useState([])

    const loadShopPolicies = async () => {
        console.log("clicked on shop policies");
        await getDiscountPolicies(props.visitorId, props.shopId).then(res => {
                if (res.errorOccurred) {
                    alert(res.errorMessage)
                    console.log(res.errorMessage)
                    return
                } else {
                    if (res.value.length === 0) {
                        setShopDiscountPolicy([])
                        alert("No Discount policy found")
                        return
                    }
                    setShopDiscountPolicy([...res.value])
                }
            }
        )
    }

    const viewAllDiscount = (e) => {
        e.preventDefault()
        loadShopPolicies()
        setViewAllD(!viewAllD);
        setAddProductD(false);
        setAddCategoryD(false);
        setAddShopD(false);
        setXORD(false);
        setPQCD(false);
        setTBPD(false);
    };

    const viewAddProductDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(!addProductD);
        setAddCategoryD(false);
        setAddShopD(false);
        setXORD(false);
        setPQCD(false);
        setTBPD(false);
    };

    const viewAddCategoryDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(false);
        setAddCategoryD(!addCategoryD);
        setAddShopD(false);
        setXORD(false);
        setPQCD(false);
        setTBPD(false);
    };

    const viewAddShopDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(false);
        setAddCategoryD(false);
        setAddShopD(!addShopD);
        setXORD(false);
        setPQCD(false);
        setTBPD(false);
    };

    const viewAddXORDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(false);
        setAddCategoryD(false);
        setAddShopD(false);
        setXORD(!addXORD);
        setPQCD(false);
        setTBPD(false);
    };

    const viewAddPQCDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(false);
        setAddCategoryD(false);
        setAddShopD(false);
        setXORD(false);
        setPQCD(!addPQCD);
        setTBPD(false);
    };

    const viewAddTBPDiscount = (e) => {
        e.preventDefault()
        setViewAllD(false);
        setAddProductD(false);
        setAddCategoryD(false);
        setAddShopD(false);
        setXORD(false);
        setPQCD(false);
        setTBPD(!addTBPD);
    };

    return (
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.ul}>
                        <li>
                            <button onClick={viewAllDiscount}>Show all discounts</button>
                        </li>
                        <li>
                            <button onClick={viewAddProductDiscount}>
                                Add product discount
                            </button>
                        </li>
                        <li>
                            <button onClick={viewAddCategoryDiscount}>
                                Add category discount
                            </button>
                        </li>
                        <li>
                            <button onClick={viewAddShopDiscount}>Add shop discount</button>
                        </li>
                        <li>
                            <button onClick={viewAddXORDiscount}>composite XOR discount</button>
                        </li>
                        <li>
                            <button onClick={viewAddPQCDiscount}>Add product quantity discount</button>
                        </li>
                        <li>
                            <button onClick={viewAddTBPDiscount}>Add total basket price discount</button>
                        </li>
                    </ul>
                </div>
            </header>
            <div>
                {viewAllD && (
                    <ViewAllDiscounts
                        discounts={shopDiscountPolicy}
                    />
                )}
                {addProductD && (
                    <AddProductDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setAddProductD}
                    />
                )}
                {addCategoryD && (
                    <AddCategoryDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setAddCategoryD}
                    />
                )}
                {addShopD && (
                    <AddTotalShopDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setAddShopD}
                    />
                )}
                {addXORD && (
                    <AddXORDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setXORD}
                    />
                )}
                {addPQCD && (
                    <AddPQCDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setPQCD}
                    />
                )}
                {addTBPD && (
                    <AddTBPDiscount
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setTBPD}
                    />
                )}
            </div>
        </div>
    );
};

export default DiscountMenu;
