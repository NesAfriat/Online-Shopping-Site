import {useState} from "react";
import classes from "../../../styling/list2.module.css";
import {getShopAssignments, getShopRoleInfo} from "../../../api";
import ViewAllRoles from "./ViewAllRoles";
import AddOwner from "./Actions/AddOwner";
import AddManager from "./Actions/AddManager";
import AddOwnerInit from "./Actions/AddOwnerInit";
import ShopWaitingAssignments from "./ShopWaitingAssignments";
import ApproveShopOwner from "./Actions/ApproveShopOwner";

const ShopRoles = (props) => {
    const [showShopRoles, setShowShopRoles] = useState(false);
    const [showAddOwner, setShowAddOwner] = useState(false);
    const [showAddManager, setShowAddManager] = useState(false);
    const [showWaitingAssignmnets, setShowWaitingAssignments] = useState(false);
    const [showApprove, setShowApprove] = useState(false);
    const [roles, setShopRoles] = useState([]);
    const [waitingAssignments, setWaitingAssignments] = useState([]);

    const loadShopRoles = async () => {
        await getShopRoleInfo(props.visitorId, props.shopId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
                return;
            } else {
                if (res.value.length === 0) {
                    alert("No roles found");
                    return;
                }
                setShopRoles([...res.value]);
            }
        });
    };

    const loadWaitingAssignments = async () => {
        await getShopAssignments(props.visitorId, props.shopId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
                return;
            } else {
                if (res.value.length === 0) {
                    alert("No waiting assignments found");
                    setWaitingAssignments([])
                    return;
                }
                setWaitingAssignments([...res.value]);
            }
        });
    };

    const showShopRolesHandler = (e) => {
        e.preventDefault();
        loadShopRoles();
        setShowShopRoles(!showShopRoles);
        setShowAddOwner(false);
        setShowAddManager(false);
        setShowWaitingAssignments(false)
        setShowApprove(false);
    };


    const viewNewManager = (e) => {
        e.preventDefault();
        setShowShopRoles(false);
        setShowAddOwner(false);
        setShowAddManager(!showAddManager);
        setShowWaitingAssignments(false)
        setShowApprove(false);
    };
    const viewApproveOwner = (e) => {
        e.preventDefault();
        setShowShopRoles(false);
        setShowAddOwner(false);
        setShowAddManager(false);
        setShowWaitingAssignments(false)
        setShowApprove(!showApprove);
    };

    const viewNewOwner = (e) => {
        e.preventDefault();
        console.log("clicked on add new owner");
        setShowShopRoles(false);
        setShowAddOwner(!showAddOwner);
        setShowAddManager(false);
        setShowWaitingAssignments(false)
        setShowApprove(false);
    };

    const viewWaitingAssignments = (e) => {
        e.preventDefault();
        console.log("clicked on show waiting assignment");
        loadWaitingAssignments()
        setShowShopRoles(false);
        setShowAddOwner(false);
        setShowAddManager(false);
        setShowWaitingAssignments(!showWaitingAssignmnets)
        setShowApprove(false);
    };

    return (
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.ul}>
                        <li className={classes.li}>
                            <button onClick={showShopRolesHandler}><h4>Shop roles</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewWaitingAssignments}><h4>Waiting for approve</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewApproveOwner}><h4>approve member</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewNewOwner}><h4>Assign new owner</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewNewManager}><h4>Assign new manager</h4></button>
                        </li>
                    </ul>
                </div>
            </header>
            <div>
                {showShopRoles && (
                    <ViewAllRoles
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        shopRoles={roles}
                        founder={props.founder}
                        reload={loadShopRoles}
                    />
                )}
                {showWaitingAssignmnets && (
                    <ShopWaitingAssignments visitorId={props.visitorId} shopId={props.shopId} assignments={waitingAssignments}/>
                )}
                {showAddOwner && (
                    // <AddOwner visitorId={props.visitorId} shopId={props.shopId}/>
                    <AddOwnerInit visitorId={props.visitorId} shopId={props.shopId}/>
                )}
                {showApprove && (
                    // <AddOwner visitorId={props.visitorId} shopId={props.shopId}/>
                    <ApproveShopOwner visitorId={props.visitorId} shopId={props.shopId}/>
                )}
                {showAddManager && (
                    <AddManager visitorId={props.visitorId} shopId={props.shopId}/>
                )}
            </div>
        </div>
    );
};

export default ShopRoles;
