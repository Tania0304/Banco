import React from "react";

import Menu from "../componentes/Header/Menu";
import Footer from "../componentes/Footer/Footer";
import Routers from "../Routes/Routers";

const Layout= () => {
    return(
        //FRAMES, no se renderizan sobre el html de la pagina, se crearan los componentes
        <>
        <Menu />
        <main className="bodys">
            <Routers />
        </main>
        <Footer />
        </>
    )
}

export default Layout