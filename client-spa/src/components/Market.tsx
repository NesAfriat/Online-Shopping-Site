import classes from "../styling/mainNavigation.module.css";
import {useState} from "react";
import Login from "./Login";
import Register from "./Register";
import ViewAllShops from "./ViewAllShops";
import Search from "./Search";
import OpenShop from "./OpenShop";
import MyCart from "./MyCart";

const Market: FC<{}> = (props) => {
  const [loginFlag, setLoginFlag] = useState(false);
  const [registerFlag, setRegisterFlag] = useState(false);
  const [viewAllShops, setViewAllShops] = useState(false);
  const [searchFlag, setSearchFlag] = useState(false);
  const [openShopFlag, setOpenShopFlag] = useState(false);
  const [myCartFlag, setMyCartFlag] = useState(false);
  const [memberUserName, setMemberUserName] = useState("");
  const [visitorCart, setVisitorCart] = useState({});


  const showLogin = (e) => {
    e.preventDefault()
    console.log(`clicked on login`);
    setLoginFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
  }

  const showRegister = (e) => {
    e.preventDefault()
    console.log(`clicked on register`);
    setRegisterFlag(true);
    setLoginFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
  }

  const showViewAllShops = (e) => {
    e.preventDefault()
    console.log(`clicked on view all shops`);
    setViewAllShops(true);
    setRegisterFlag(false);
    setLoginFlag(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
  }

  const showSearch = (e) => {
    e.preventDefault()
    console.log(`clicked on search`);
    setSearchFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
  }

  const showOpenShop = (e) => {
    e.preventDefault()
    console.log(`clicked on open shop`);
    setOpenShopFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setMyCartFlag(false);
  }

  const showMyCart = (e) => {
    e.preventDefault()
    console.log(`clicked on my cart`);
    setMyCartFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
  }


  return (
      <div>
        <header className={classes.header}>
          <div className={classes.logo}>
            <ul>
              <li>
                <button onClick={showLogin}>
                  <h3 className={classes.h3}>Login</h3>
                </button>
              </li>
              <li>
                <button onClick={showRegister}>
                  <h3 className={classes.h3}>Register</h3>
                </button>
              </li>
              <li>
                <button onClick={showViewAllShops}>
                  <h3 className={classes.h3}>View all shops</h3>
                </button>
              </li>
              <li>
                <button onClick={showSearch}>
                  <h3 className={classes.h3}>Search products</h3>
                </button>
              </li>
              <li>
                <button onClick={showOpenShop}>
                  <h3 className={classes.h3}>Open new shop</h3>
                </button>
              </li>
              <li>
                <button onClick={showMyCart}>
                  <h3 className={classes.h3}>My cart</h3>
                </button>
              </li>
              <li>
                <button>
                  <h3 className={classes.h3}>Leave</h3>
                </button>
              </li>
            </ul>
          </div>
        </header>
        {loginFlag && <Login visitorId={props.visitorId} setter={setLoginFlag}
                             setMember={setMemberUserName}
                             setCart={setVisitorCart}/>}
        {registerFlag && <Register visitorId={props.visitorId} setter={setRegisterFlag}/>}
        {viewAllShops && <ViewAllShops visitorId={props.visitorId} setter={setViewAllShops}/>}
        {searchFlag && <Search visitorId={props.visitorId} setter={setSearchFlag}/>}
        {openShopFlag && <OpenShop visitorId={props.visitorId} setter={setOpenShopFlag}/>}
        {myCartFlag && <MyCart visitorId={props.visitorId} setter={setMyCartFlag}/>}
      </div>
  );
};


export default Market;
