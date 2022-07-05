import "./App.css";
import { useState } from "react";
import {
  postVisit,
  postLogout,
  getAllOpenShops,
  getMyShops,
  postLeave, getShoppingCart, SocketProvider, getMyBids
} from "./api";
import classes from "./styling/mainNavigation.module.css";
import Login from "./components/Login";
import Register from "./components/Register";
import ViewAllShops from "./components/ViewAllShops";
import Search from "./components/Search";
import OpenShop from "./components/OpenShop";
import MyCart from "./components/MyCart/MyCart";
import ViewMyBids from "./components/MyBids/ViewMyBids";
import ViewMyShops from "./components/MyShops/ViewMyShops";
import AdminPage from "./components/AdminPage/AdminPage";

const App = () => {
  const [visitorId, setVisitorId] = useState(-1);
  const [loginFlag, setLoginFlag] = useState(false);
  const [registerFlag, setRegisterFlag] = useState(false);
  const [viewAllShops, setViewAllShops] = useState(false);
  const [viewMyShops, setViewMyShops] = useState(false);
  const [viewAdminPage, setViewAdminPage] = useState(false);
  const [viewMyBids, setViewMyBids] = useState(false)
  const [searchFlag, setSearchFlag] = useState(false);
  const [openShopFlag, setOpenShopFlag] = useState(false);
  const [myCartFlag, setMyCartFlag] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [memberUserName, setMemberUserName] = useState("");
  const [shops, setShops] = useState([]);
  const [myShops, setMyShops] = useState([]);
  const [shoppingCart, setShoppingCart] = useState(null);
  const [socketProvider, setSocketProvider] = useState(new SocketProvider());
  const [myBids, setMyBids] = useState([])

  const visitHandler = async () => {
    console.log(`clicked on visit`);
    await postVisit().then((res) => {
      // console.log(res);
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
      } else {
        setVisitorId(res.value.id);
      }
    });
  };

  const leaveMarket = async () => {
    console.log("commiting leave");
    await postLeave(visitorId).then((res) => {
      // console.log(res);
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
      } else {
        setVisitorId(-1);
      }
    });
  };

  const loadMyShops = async () => {
    console.log("clicked on my shops");
    await getMyShops(visitorId).then((res) => {
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
        return;
      } else {
        if (res.value.length === 0) {
          setMyShops([]);
          alert("No shop found");
          return;
        }
        setMyShops([...res.value]);
      }
    });
  };

  const loadMyBids = async () => {
    console.log("clicked on my bids");
    await getMyBids(visitorId).then((res) => {
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
        return;
      } else {
        if (res.value.length === 0) {
          setMyBids([]);
          alert("No bids found");
          return;
        }
        setMyBids([...res.value]);
      }
    });
  };

  const reloadBids = async () => {
    await getMyBids(visitorId).then((res) => {
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
        return;
      } else {
        if (res.value.length === 0) {
          setMyBids([]);
          return;
        }
        setMyBids([...res.value]);
      }
    });
  };

  const logout = async () => {
    console.log(`clicked on logout`);
    socketProvider.disconnect();
    await postLogout(visitorId).then((res) => {
      // console.log(res);
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
        return;
      } else {

        setMemberUserName("");
      }
    });
  };
  const handleLogout = () => {
    logout();
    setLoginFlag(false);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setIsAdmin(false);
    setViewAdminPage(false);
    setViewMyBids(false)
  };

  const loadShops = async () => {
    await getAllOpenShops(visitorId).then((res) => {
      if (res.errorOccurred) {
        alert(res.errorMessage);
        console.log(res.errorMessage);
        return;
      } else {
        if (res.value.length === 0) {
          setShops([]);
          alert("No shop found");
          return;
        }
        setShops([...res.value]);
      }
    });
  };

  const loadMyCart = async () => {
    await getShoppingCart(visitorId).then((res) => {
      if (res.errorOccurred) {
        alert(res.errorMessage);
        return;
      } else {
        if (res.value.length === 0) {
          setShoppingCart(null);
          return;
        }
        setShoppingCart(res.value);
      }
    });
  };

  const showLogin = (e) => {
    e.preventDefault();
    console.log(`clicked on login`);
    setLoginFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false)
  };

  const showRegister = (e) => {
    e.preventDefault();
    console.log(`clicked on register`);
    setRegisterFlag(true);
    setLoginFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);

  };

  const showViewAllShops = (e) => {
    e.preventDefault();
    console.log(`clicked on view all shops`);
    loadShops();
    setViewAllShops(true);
    setRegisterFlag(false);
    setLoginFlag(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);
  };

  const showMyShops = (e) => {
    e.preventDefault();
    console.log(`clicked on view My shops`);
    loadMyShops();
    setViewMyShops(true);
    setViewAllShops(false);
    setRegisterFlag(false);
    setLoginFlag(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewAdminPage(false);
    setViewMyBids(false);
  };

  const showMyBids = (e) => {
    e.preventDefault();
    console.log(`clicked on view My shops`);
    loadMyBids();
    setViewMyBids(true);
    setViewMyShops(false);
    setViewAllShops(false);
    setRegisterFlag(false);
    setLoginFlag(false);
    setSearchFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewAdminPage(false);
  };

  const showSearch = (e) => {
    e.preventDefault();
    console.log(`clicked on search`);
    setSearchFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);
  };

  const showOpenShop = (e) => {
    e.preventDefault();
    console.log(`clicked on open shop`);
    setOpenShopFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setMyCartFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);
  };

  const showMyCart = (e) => {
    e.preventDefault();
    console.log(`clicked on my cart`);
    loadMyCart();
    setMyCartFlag(true);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);
  };

  const showAdminPage = (e) => {
    e.preventDefault();
    console.log(`clicked on admin page`);
    setMyCartFlag(false);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
    setViewMyShops(false);
    setViewAdminPage(true);
    setViewMyBids(false);
  };

  const setLeave = (e) => {
    e.preventDefault();
    console.log(`clicked on leave`);
    setMyCartFlag(false);
    setRegisterFlag(false);
    setViewAllShops(false);
    setSearchFlag(false);
    setLoginFlag(false);
    setOpenShopFlag(false);
    setViewMyShops(false);
    setViewAdminPage(false);
    setViewMyBids(false);

    if (memberUserName !== "") {
      console.log("not logged out yet");
      setMemberUserName("");
      logout();
      setIsAdmin(false);
    }
    leaveMarket();
  };

  return (
    <div>
      {visitorId === -1 ? (
        <>
          <button onClick={visitHandler} className={classes.logoVisit}>visit market</button>
        </>
      ) : (
        <>
          <div>
            <header className={classes.header}>
              <div className={classes.logo}>
                <ul>
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
                    <button onClick={showMyCart}>
                      <h3 className={classes.h3}>My cart</h3>
                    </button>
                  </li>
                  {memberUserName === "" ? (
                    <li>
                      <button onClick={showLogin}>
                        <h3 className={classes.h3}>Login</h3>
                      </button>
                    </li>
                  ) : (
                    <ul>
                      <li>
                        <button onClick={showOpenShop}>
                          <h3 className={classes.h3}>Open new shop</h3>
                        </button>
                      </li>
                      <li>
                        <button onClick={showMyShops}>
                          <h3 className={classes.h3}>My shops</h3>
                        </button>
                      </li>
                      <li>
                      <button onClick={showMyBids}>
                        <h3 className={classes.h3}>My bids</h3>
                      </button>
                    </li>
                      <li>
                        <button onClick={handleLogout}>
                          <h3 className={classes.h3}>Logout</h3>
                        </button>
                      </li>
                    </ul>
                  )}
                  <li>
                    <button onClick={showRegister}>
                      <h3 className={classes.h3}>Register</h3>
                    </button>
                  </li>
                  {isAdmin && (
                    <li>
                      <button onClick={showAdminPage}>
                        <h3 className={classes.h3}>Admin actions</h3>
                      </button>
                    </li>
                  )}
                  <li>
                    <button onClick={setLeave}>
                      <h3 className={classes.h3}>Leave</h3>
                    </button>
                  </li>
                </ul>
              </div>
            </header>
            {loginFlag && (
              <Login
                visitorId={visitorId}
                setter={setLoginFlag}
                setMember={setMemberUserName}
                setIsAdmin= {setIsAdmin}
                socketProvider={socketProvider}
              />
            )}
            {registerFlag && (
              <Register visitorId={visitorId} setter={setRegisterFlag} />
            )}
            {viewAllShops && shops.length > 0 && (
              <ViewAllShops
                visitorId={visitorId}
                setter={setViewAllShops}
                shops={shops}
                isAdmin={isAdmin}
                member={memberUserName}
              />
            )}
            {viewMyShops && (
              <ViewMyShops
                visitorId={visitorId}
                setter={setViewMyShops}
                shops={myShops}
                memberUserName={memberUserName}

              />
            )}
            {viewMyBids && (
                <ViewMyBids
                    visitorId={visitorId}
                    setter={setViewMyBids}
                    bids={myBids}
                    memberUserName={memberUserName}
                    reloadBids={reloadBids}
                />
            )}
            {searchFlag && (
              <Search visitorId={visitorId} setter={setSearchFlag} member={memberUserName}/>
            )}
            {openShopFlag && (
              <OpenShop visitorId={visitorId} setter={setOpenShopFlag} />
            )}
            {myCartFlag && shoppingCart && (
              <MyCart visitorId={visitorId} setter={setMyCartFlag} cart={shoppingCart} reloadCart={loadMyCart}/>
            )}

            {viewAdminPage && (
              <AdminPage visitorId={visitorId} setter={setViewAdminPage} />
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default App;
