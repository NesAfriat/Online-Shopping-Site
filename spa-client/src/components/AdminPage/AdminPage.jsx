import {useState} from "react";
import {getAllMembers, getAllShopOwners, getAllVisitors} from "../../api";
import classes from "../../styling/list2.module.css";
import ViewAllVisitors from "./ViewAllVisitors";
import ViewAllOwners from "./ViewAllOwners";
import ViewAllMembers from "./ViewAllMembers";
import ViewDailyData from "./ViewDailyData";

const AdminPage = (props) => {
    const [allVisitors, setAllVisitors] = useState([]);
    const [allMembers, setAllMembers] = useState([]);
    const [allOwners, setAllOwners] = useState([]);
    const [showVisitors, setShowVisitors] = useState(false);
    const [showMembers, setShowMembers] = useState(false);
    const [showOwners, setShowOwners] = useState(false);
    const [showDailyData, setShowDailyData] = useState(false);


    const loadAllVisitors = async () => {
        console.log("loading visitors");
        await getAllVisitors(props.visitorId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
                return;
            } else {
                if (res.value.length === 0) {
                    setAllVisitors([]);
                    alert("No visitors found");
                    return;
                }
                setAllVisitors([...res.value]);
            }
        });
    };
    const loadAllMembers = async () => {
        console.log("loading members");
        await getAllMembers(props.visitorId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
                return;
            } else {
                if (res.value.length === 0) {
                    setAllMembers([]);
                    alert("No members found");
                    return;
                }
                setAllMembers([...res.value]);
            }
        });
    };

    const loadAllOwners = async () => {
        console.log("loading owners");
        await getAllShopOwners(props.visitorId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
                return;
            } else {
                if (res.value.length === 0) {
                    setAllOwners([]);
                    alert("No visitors found");
                    return;
                }
                setAllOwners([...res.value]);
            }
        });
    };


    const viewAllVisitors = (e) => {
        e.preventDefault();
        loadAllVisitors();
        setShowVisitors(!showVisitors);
        setShowMembers(false);
        setShowOwners(false);
        setShowDailyData(false);
    };

    const viewAllMembers = (e) => {
        e.preventDefault();
        loadAllMembers();
        setShowVisitors(false);
        setShowMembers(!showMembers);
        setShowOwners(false);
        setShowDailyData(false);
    };

    const viewAllOwners = (e) => {
        e.preventDefault();
        loadAllOwners();
        setShowVisitors(false);
        setShowMembers(false);
        setShowOwners(!showOwners);
        setShowDailyData(false);
    };

    const viewDailyData = (e) => {
        e.preventDefault();
        setShowVisitors(false);
        setShowMembers(false);
        setShowOwners(false);
        setShowDailyData(!showDailyData);
    };


    return <>
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.ul}>
                        <li className={classes.li}>
                            <button onClick={viewAllVisitors}><h4>View all visitors</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewAllMembers}><h4>View all members</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewAllOwners}><h4>View all shop owners</h4></button>
                        </li>
                        <li className={classes.li}>
                            <button onClick={viewDailyData}><h4>View daily data</h4></button>
                        </li>

                    </ul>
                </div>
            </header>
            <div>
                {showVisitors && (
                    <ViewAllVisitors
                        visitorId={props.visitorId}
                        visitors={allVisitors}
                    />
                )}
                {showOwners && (
                    <ViewAllOwners
                        visitorId={props.visitorId}
                        owners={allOwners}
                    />
                )}
                {showMembers && (
                    <ViewAllMembers
                        visitorId={props.visitorId}
                        members={allMembers}
                        reload={loadAllMembers}
                    />
                )}
                {showDailyData && (
                    <ViewDailyData
                        visitorId={props.visitorId}
                    />
                )}
            </div>
        </div>
    </>
};

export default AdminPage;