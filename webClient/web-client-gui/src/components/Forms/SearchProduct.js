import { useRef } from "react";
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { useState } from "react";

function SearchProduct() {
  const [validInput, setValid] = useState(true);
  const searchInputRef = useRef();
  const [typeValue, typeInputProps] = useRadioButtons("searchType");

  function useRadioButtons(name) {
      const [value, setState] = useState(null);
    
      const handleChange = e => {
        setState(e.target.value);
      };
    
      const inputProps = {
        name,
        type: "radio",
        onChange: handleChange
      };
      return [value, inputProps];
    }
  function failInput() {
    alert("illegal input, please try again");
    history.replace("/search-product");
  }
  function Succes() {
    alert("The action completed succesfuly");

  }
  function cancelHandler() {
    history.replace('/market');
  }
  function searchPrductByName(SearchData) {
    ///axious take data and get it to the server
    //invalid input:
    //setValid(false)
    setValid(true);
  }
  function searchPrductByCategory(SearchData) {
    ///axious take data and get it to the server
    //invalid input:
    //setValid(false)
    setValid(true);
  }
  function searchPrductByKeyWord(SearchData) {
    ///axious take data and get it to the server
    //invalid input:
    //setValid(false)
    setValid(true);
  }
  function submitHandler(event) {
    event.preventDefault();
    const enteredSearchInput = searchInputRef.current.value;

    const SearchData = {
      searchInput: enteredSearchInput,
    };

    if (typeValue === "name") searchPrductByName(SearchData);
    else if (typeValue === "category") searchPrductByCategory(SearchData);
    else searchPrductByKeyWord(SearchData);
    validInput ? Succes() : failInput();
  }
  return (
    <div>
    <form onSubmit={submitHandler}>
      <div className={classes.control}>
        <h1 className={classes.h1}>Choose the search type and complete your search:</h1>
        <label htmlFor="search-input">enter product information: </label>
        <input type="text" required id="search-input" ref={searchInputRef} />
      </div>
      <fieldset>
          Search by name
          <input
            value="name"
            checked={typeValue === "name"}
            {...typeInputProps}
          />
          Search by category
          <input
            value="category"
            checked={typeValue === "category"}
            {...typeInputProps}
          />
          Search by keyword
          <input
            value="keyword"
            checked={typeValue === "keyword"}
            {...typeInputProps}
          />
        </fieldset>
      <div>
        <button className={classes.actions}>Search</button>
      </div>
    </form>
        <button className={classes.returnButton} onClick={cancelHandler}>
        Return
      </button>
      </div>
  );
}

export default SearchProduct;
