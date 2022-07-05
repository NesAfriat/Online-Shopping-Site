import { Link } from "react-router-dom";
import classes from "./mainNavigation.module.css";
function MarketNavigation(props) {
  return (
    <header className={classes.header}>
      <div className={classes.logo}>
        <nav>
          <ul>
            <li>
              <Link to="/login">
                <h3 className={classes.h3}>Login</h3>
              </Link>
            </li>
            <li>
              <Link to="/register">
                <h3 className={classes.h3}>Register</h3>
              </Link>
            </li>
            <li>
              <Link to="/all-shops">
                <h3 className={classes.h3}>View all shops</h3>
              </Link>
            </li>
            <li>
              <Link to="/search-product">
                <h3 className={classes.h3}>Search products</h3>
              </Link>
            </li>
            <li>
              <Link to="/open-shop">
                <h3 className={classes.h3}>Open new shop</h3>
              </Link>
            </li>{" "}
            <li>
              <Link to="/my-cart">
                <h3 className={classes.h3}>My cart</h3>
              </Link>
            </li>
            <li>
              <Link to="/" onClick={props.leaveSystem}>
                <h3 className={classes.h3}>Leave</h3>
              </Link>
            </li>
          </ul>
        </nav>
      </div>
    </header>
  );
}

export default MarketNavigation;
