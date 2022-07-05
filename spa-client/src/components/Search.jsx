import {useRef, useState} from "react";
import classes from "../styling/addForm.module.css";
import {getSearchProductByCategory, getSearchProductByKeyWord, getSearchProductByName,} from "../api";
import Product from "./BasicComponent/GeneralProduct";

const Search = (props) => {
    const searchInputRef = useRef();
    const [products, setProducts] = useState([]);

    const useRadioButtons = (name) => {
        const [value, setState] = useState(null);

        const handleChange = (e) => {
            setState(e.target.value);
        };

        const inputProps = {
            name,
            type: "radio",
            onChange: handleChange,
        };

        return [value, inputProps];
    };

    const [typeValue, typeInputProps] = useRadioButtons("searchType");

    const submitHandler = async (e) => {
        e.preventDefault();
        const searchInput = searchInputRef.current.value;

        if (typeValue === "name") {
            await getSearchProductByName(props.visitorId, searchInput).then((res) => {
                console.log(res);
                if (res.errorOccurred) {
                    alert(res.errorMessage);
                    console.log(res.errorMessage)
                } else {
                    if (res.value.length === 0) {
                        setProducts([]);
                        alert("No products found");
                        return;
                    }
                    setProducts([...res.value]);
                }
            });
        } else if (typeValue === "category") {
            await getSearchProductByCategory(props.visitorId, searchInput).then(
                (res) => {
                    console.log(`search for category: ${searchInput}`)
                    console.log(res);
                    if (res.errorOccurred) {
                        alert(res.errorMessage);
                        console.log(res.errorMessage)
                    } else {
                        if (res.value.length === 0) {
                            setProducts([]);
                            alert("No products found");
                            return;
                        }
                        setProducts([...res.value]);
                    }
                }
            );
        } else {
            await getSearchProductByKeyWord(props.visitorId, searchInput).then(
                (res) => {
                    console.log(res);
                    if (res.errorOccurred) {
                        alert(res.errorMessage);
                        console.log(res.errorMessage)
                    } else {
                        if (res.value.length === 0) {
                            setProducts([]);
                            alert("No products found");
                            return;
                        }
                        setProducts([...res.value]);
                    }
                }
            );
        }
    };
    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>
                        Choose the search type and complete your search:
                    </h1>
                </div>
                <fieldset>
                    Search by:
                    <input
                        value="name"
                        checked={typeValue === "name"}
                        {...typeInputProps}
                    />{" "}
                    name
                    <input
                        value="category"
                        checked={typeValue === "category"}
                        {...typeInputProps}
                    />{" "}
                    category
                    <input
                        value="keyword"
                        checked={typeValue === "keyword"}
                        {...typeInputProps}
                    />{" "}
                    keyword
                </fieldset>
                <label htmlFor="search-input" className={classes.control}>Enter product information: </label>
                <input type="text" required id="search-input" ref={searchInputRef}/>
                <div>
                    <button className={classes.actions}>Search</button>
                </div>
            </form>
            <ul>
                {products &&
                    products.length > 0 &&
                    products.map((prod) => {
                        return (
                            <li key={prod.id}>
                                <Product
                                    visitorId={props.visitorId}
                                    id={prod.id}
                                    shopId={prod.shopId}
                                    name={prod.productName}
                                    description={prod.description}
                                    category={prod.category}
                                    quantity={prod.quantity}
                                    price={prod.price}
                                    member={props.member}
                                />
                            </li>
                        );
                    })}
            </ul>
        </div>
    );
};

export default Search;
