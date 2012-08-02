/**
 * Copyright (C) 2012 Enterprise System Solutions (P) Ltd. All rights reserved.
 *
 * This file is part of DATA Gen. http://testdatagen.sourceforge.net/
 *
 * DATA Gen is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DATA Gen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.esspl.datagen.common.JdbcTable;

/**
 * @author Tapas
 *
 */
public class MetadataRetriever {

    private final DatabaseMetaData metaData;

    public MetadataRetriever(Connection connection) throws SQLException {
        this.metaData = connection.getMetaData();
    }

    public List<String> getCatalogs() throws SQLException {
        List<String> rez = new ArrayList<String>();
        ResultSet catalogs = metaData.getCatalogs();
        while (catalogs.next())
            rez.add(catalogs.getString("TABLE_CAT"));
        return rez;
    }

    public String getCatalogTerm() throws SQLException {
        String term = metaData.getCatalogTerm();
        return null == term || term.isEmpty() ? "catalog" : term;
    }

    public List<String> getSchemas() throws SQLException {
        List<String> rez = new ArrayList<String>();
        ResultSet schemas = metaData.getSchemas();
        while (schemas.next())
            rez.add(schemas.getString("TABLE_SCHEM"));
        schemas.close();
        return rez;
    }

    public String getSchemaTerm() throws SQLException {
        String term = metaData.getSchemaTerm();
        return null == term || term.isEmpty() ? "schema" : term;
    }

    public List<String> getTableTypes() throws SQLException {
        List<String> rez = new ArrayList<String>();
        ResultSet types = metaData.getTableTypes();
        while (types.next())
            rez.add(types.getString("TABLE_TYPE"));
        types.close();
        return rez;
    }

    public List<JdbcTable> getTables(String catalog, String schema, String tableType) throws SQLException {
        ArrayList<JdbcTable> rez = new ArrayList<JdbcTable>();
        ResultSet tables = metaData.getTables(catalog, schema, null, new String[]{tableType});
        while (tables.next()){
        	String tableName = tables.getString("TABLE_NAME");
        	//Special Checking add as in Oracle it is showing system tables as well
        	if(tableName.indexOf("$") == -1 && tableName.toLowerCase().indexOf("htmldb_plan_table") == -1){
        		rez.add(new JdbcTable(tables.getString("TABLE_CAT"), tables.getString("TABLE_SCHEM"), tableName));
        	}
        }
        tables.close();
        return rez;
    }
}
