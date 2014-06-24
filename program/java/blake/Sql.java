package blake;

/**
 * 
 * @author Blake Lin
 *
 */
public class Sql {
	
	private StringBuffer sb = new StringBuffer();
	
	/**
	 * examples
	 * @param args
	 */
	public static final void main(String[] args) {
		Sql sql = new Sql();
		
		
		
		sql.insert(
			"user",
			new String[]{"account",  "password"},
			new Object[]{"testUser", "testUserPassword"}
		);
		System.out.println("Insert: " + sql.getString());
		
		
		
		sql.insert(
			"user",
			new String[]{"account", "password"}
		);
		System.out.println("Insert: " + sql.getString());
		
		
		
		sql.update(
			"user",
			new String[]{"account",   "password"},
			new Object[]{"testUser2", "testUser2Password"}
		).where(
			"account = 'testUser'"
		);
		System.out.println("Update: " + sql.getString());
		
		
		
		sql.update(
			"user",
			new String[]{"account",   "password"}
		).where(
			"account = 'testUser'"
		);
		System.out.println("Update: " + sql.getString());
		
		
		
		sql.delete(
			"user"
		).where(
			"account = 'testUser2'"
		);
		System.out.println("Delete: " + sql.getString());
		
		
		
		sql.select(
			new String[]{"account"}
		).from(
			new String[]{"user"}
		).where(
			"account = 'testUser'"
		).groupBy(
			new String[]{"account"}
		).orderBy(
			new String[]{"account"},
			new int[]{Sql.OrderBy.SORT_TYPE_ASC}
		);
		System.out.println("Select: " + sql.getString());
		
		
		
		sql.select(
			new String[]{"account"}
		).from(
			"user"
		).leftJoin(
			"player"
		).on(
			"user.id = player.uid"
		);
		System.out.println("Select: " + sql.getString());
	}
	
	public Sql.Insert insert(String table, String[] fieldName, Object[] values) {
		this.deleteAll();
		return new Sql.Insert(this, table, fieldName, values);
	}
	
	public Sql.Insert insert(String table, String[] fieldName) {
		this.deleteAll();
		return new Sql.Insert(this, table, fieldName, null);
	}
	
	public Sql.Insert insert(String database, String table, String[] fieldName, Object[] values) {
		this.deleteAll();
		return new Sql.Insert(this, database, table, fieldName, values);
	}
	
	public Sql.Insert insert(String database, String table, String[] fieldName) {
		this.deleteAll();
		return new Sql.Insert(this, database, table, fieldName, null);
	}
	
	public Sql.Update update(String table, String[] fieldName, Object[] values) {
		this.deleteAll();
		return new Sql.Update(this, table, fieldName, values);
	}
	
	public Sql.Update update(String table, String[] fieldName) {
		this.deleteAll();
		return new Sql.Update(this, table, fieldName, null);
	}
	
	public Sql.Update update(String database, String table, String[] fieldName, Object[] values) {
		this.deleteAll();
		return new Sql.Update(this, database, table, fieldName, values);
	}
	
	public Sql.Update update(String database, String table, String[] fieldName) {
		this.deleteAll();
		return new Sql.Update(this, database, table, fieldName, null);
	}
	
	public Sql.Delete delete(String table) {
		this.deleteAll();
		return new Sql.Delete(this, table);
	}
	
	public Sql.Delete delete(String database, String table) {
		this.deleteAll();
		return new Sql.Delete(this, database, table);
	}
	
	public Sql.Select select(String[] fieldName) {
		this.deleteAll();
		return new Sql.Select(this, fieldName);
	}
	
	private void deleteAll() {
		this.sb.delete(0, this.sb.length());
	}
	
	public String getString() {
		return this.sb.toString();
	}
	
	public String getString(String rename) {
		StringBuffer sb = new StringBuffer(this.sb.toString());
		
		sb.insert(0, '(');
		sb.append(')');
		sb.append('`');
		sb.append(rename);
		sb.append('`');
		
		return sb.toString();
	}
	
	public class Insert {
		
		private Sql sql;
		
		private Insert(Sql sql) {
			this.sql = sql;
		}
		
		private Insert(Sql sql, String table, String[] fieldName, Object[] values) {
			this(sql);
			sql.sb.append(" INSERT INTO ");
			sql.sb.append(table);
			for(int a = 0; a < fieldName.length; a++) {
				if(a == 0) {
					sql.sb.append('(');
				}
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append('`');
				sql.sb.append(fieldName[a]);
				sql.sb.append('`');
				if(a == fieldName.length - 1) {
					sql.sb.append(')');
				}
			}
			sql.sb.append(" VALUES ");
			if(values == null) {
				for(int a = 0; a < fieldName.length; a++) {
					if(a == 0) {
						sql.sb.append('(');
					}
					if(a != 0) {
						sql.sb.append(',');
						sql.sb.append(' ');
					}
					sql.sb.append('?');
					if(a == fieldName.length - 1) {
						sql.sb.append(')');
					}
				}
			}
			else {
				for(int a = 0; a < values.length; a++) {
					if(a == 0) {
						sql.sb.append('(');
					}
					if(a != 0) {
						sql.sb.append(',');
						sql.sb.append(' ');
					}
					if(values[a] == null) {
						sql.sb.append('?');
					}
					else {
						sql.sb.append(values[a].toString());
					}
					if(a == fieldName.length - 1) {
						sql.sb.append(')');
					}
				}
			}
		}
		
		private Insert(Sql sql, String database, String table, String[] fieldName, Object[] values) {
			this(sql, database + '.' + table, fieldName, values);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
	}
	
	public class Update {
		
		private Sql sql;
		
		private Update(Sql sql) {
			this.sql = sql;
		}
		
		private Update(Sql sql, String table, String[] fieldName, Object[] values) {
			this(sql);
			sql.sb.append(" UPDATE ");
			sql.sb.append(table);
			sql.sb.append(" SET ");
			for(int a = 0; a < fieldName.length; a++) {
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append(fieldName[a]);
				sql.sb.append(' ');
				sql.sb.append('=');
				sql.sb.append(' ');
				if(values == null || values[a] == null) {
					sql.sb.append('?');
				}
				else {
					sql.sb.append(values[a].toString());
				}
			}
		}
		
		private Update(Sql sql, String database, String table, String[] fieldName, Object[] values) {
			this(sql, database + '.' + table, fieldName, values);
		}
		
		public Sql.UpdateWhere where(String where) {
			return new Sql.UpdateWhere(this.sql, where);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
	}
	
	public class UpdateWhere {
		
		private Sql sql;
		
		private UpdateWhere(Sql sql) {
			this.sql = sql;
		}
		
		private UpdateWhere(Sql sql, String where) {
			this(sql);
			sql.sb.append(" WHERE ");
			sql.sb.append(where);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
	}
	
	public class Delete {
		
		private Sql sql;
		
		private Delete(Sql sql) {
			this.sql = sql;
		}
		
		private Delete(Sql sql, String table) {
			this(sql);
			sql.sb.append(" DELETE FROM ");
			sql.sb.append(table);
		}
		
		private Delete(Sql sql, String database, String table) {
			this(sql, database + '.' + table);
		}
		
		public Sql.DeleteWhere where(String where) {
			return new Sql.DeleteWhere(this.sql, where);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
	}
	
	public class DeleteWhere {
		
		private Sql sql;
		
		private DeleteWhere(Sql sql) {
			this.sql = sql;
		}
		
		private DeleteWhere(Sql sql, String where) {
			this(sql);
			sql.sb.append(" WHERE ");
			sql.sb.append(where);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
	}
	
	public class Select {
		
		private Sql sql;
		
		private Select(Sql sql) {
			this.sql = sql;
		}
		
		private Select(Sql sql, String[] fieldName) {
			this(sql);
			sql.sb.append(" SELECT ");
			for(int a = 0; a < fieldName.length; a++) {
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append(fieldName[a]);
			}
		}
		
		public Sql.From from(String[] table) {
			return new Sql.From(this.sql, table);
		}
		
		public Sql.From from(String table) {
			return this.from(new String[]{table});
		}
		
	}
	
	public class From {
		
		private Sql sql;
		
		private From(Sql sql) {
			this.sql = sql;
		}
		
		private From(Sql sql, String[] table) {
			this(sql);
			sql.sb.append(" FROM ");
			for(int a = 0; a < table.length; a++) {
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append(table[a]);
			}
		}
		
		public Sql.Where where(String where) {
			return new Sql.Where(this.sql, where);
		}
		
		public Sql.GroupBy groupBy(String[] fieldName) {
			return new Sql.GroupBy(this.sql, fieldName);
		}
		
		public Sql.OrderBy orderBy(String[] fieldName, int[] sortType) {
			return new Sql.OrderBy(this.sql, fieldName, sortType);
		}
		
		public Sql.Limit limit(int row_count) {
			return new Sql.Limit(this.sql, row_count);
		}
		
		public Sql.Limit limit(int offset, int row_count) {
			return new Sql.Limit(this.sql, offset, row_count);
		}
		
		public Sql.Limit limit(String limit) {
			return new Sql.Limit(this.sql, limit);
		}
		
		public Sql.LeftJoin leftJoin(String table) {
			return new Sql.LeftJoin(this.sql, table);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
	public class Where {
		
		private Sql sql;
		
		private Where(Sql sql) {
			this.sql = sql;
		}
		
		private Where(Sql sql, String where) {
			this(sql);
			sql.sb.append(" WHERE ");
			sql.sb.append(where);
		}
		
		public Sql.GroupBy groupBy(String[] fieldName) {
			return new Sql.GroupBy(this.sql, fieldName);
		}
		
		public Sql.OrderBy orderBy(String[] fieldName, int[] sortType) {
			return new Sql.OrderBy(this.sql, fieldName, sortType);
		}
		
		public Sql.Limit limit(int row_count) {
			return new Sql.Limit(this.sql, row_count);
		}
		
		public Sql.Limit limit(int offset, int row_count) {
			return new Sql.Limit(this.sql, offset, row_count);
		}
		
		public Sql.Limit limit(String limit) {
			return new Sql.Limit(this.sql, limit);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
	public class GroupBy {
		
		private Sql sql;
		
		private GroupBy(Sql sql) {
			this.sql = sql;
		}
		
		private GroupBy(Sql sql, String[] fieldName) {
			this(sql);
			sql.sb.append(" GROUP BY ");
			for(int a = 0; a < fieldName.length; a++) {
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append(fieldName[a]);
			}
		}
		
		public Sql.OrderBy orderBy(String[] fieldName, int[] sortType) {
			return new Sql.OrderBy(this.sql, fieldName, sortType);
		}
		
		public Sql.Limit limit(int row_count) {
			return new Sql.Limit(this.sql, row_count);
		}
		
		public Sql.Limit limit(int offset, int row_count) {
			return new Sql.Limit(this.sql, offset, row_count);
		}
		
		public Sql.Limit limit(String limit) {
			return new Sql.Limit(this.sql, limit);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
	public class OrderBy {
		
		public static final int SORT_TYPE_ASC = 0;
		public static final int SORT_TYPE_DESC = 1;
		
		private Sql sql;
		
		private OrderBy(Sql sql) {
			this.sql = sql;
		}
		
		private OrderBy(Sql sql, String[] fieldName, int[] sortType) {
			this(sql);
			sql.sb.append(" ORDER BY ");
			for(int a = 0; a < fieldName.length; a++) {
				if(a != 0) {
					sql.sb.append(',');
					sql.sb.append(' ');
				}
				sql.sb.append(fieldName[a]);
				sql.sb.append(' ');
				switch(sortType[a]) {
				case Sql.OrderBy.SORT_TYPE_DESC:
					sql.sb.append("DESC");
					break;
				
				default:
					sql.sb.append("ASC");
					break;
				}
			}
		}
		
		public Sql.Limit limit(int row_count) {
			return new Sql.Limit(this.sql, row_count);
		}
		
		public Sql.Limit limit(int offset, int row_count) {
			return new Sql.Limit(this.sql, offset, row_count);
		}
		
		public Sql.Limit limit(String limit) {
			return new Sql.Limit(this.sql, limit);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
	public class Limit {
		
		private Sql sql;
		
		private Limit(Sql sql) {
			this.sql = sql;
		}
		
		private Limit(Sql sql, int row_count) {
			this(sql);
			sql.sb.append(" LIMIT ");
			sql.sb.append(row_count);
		}
		
		private Limit(Sql sql, int offset, int row_count) {
			this(sql);
			sql.sb.append(" LIMIT ");
			sql.sb.append(offset);
			sql.sb.append(',');
			sql.sb.append(' ');
			sql.sb.append(row_count);
		}
		
		private Limit(Sql sql, String limit) {
			this(sql);
			sql.sb.append(" LIMIT ");
			sql.sb.append(limit);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
	public class LeftJoin {
		
		private Sql sql;
		
		private LeftJoin(Sql sql) {
			this.sql = sql;
		}
		
		private LeftJoin(Sql sql, String table) {
			this(sql);
			sql.sb.append(" LEFT JOIN ");
			sql.sb.append(table);
		}
		
		public Sql.On on(String on) {
			return new Sql.On(this.sql, on);
		}
		
	}
	
	public class On {
		
		private Sql sql;
		
		private On(Sql sql) {
			this.sql = sql;
		}
		
		private On(Sql sql, String on) {
			this(sql);
			this.sql.sb.append(" ON ");
			this.sql.sb.append(on);
		}
		
		public String getString() {
			return this.sql.getString();
		}
		
		public String getString(String rename) {
			return this.sql.getString(rename);
		}
		
	}
	
}
