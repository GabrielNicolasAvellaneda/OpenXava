package org.openxava.test.tests

import org.openxava.tests.*

/**
 * 
 * @author Jeromy Altuna
 */

class UserWithNicknameTest extends ModuleTestBase {
	
	UserWithNicknameTest(String testName) {
		super(testName, "UserWithNickname")
	}
	
	void testUniqueConstraintsMessages() {
		assertListRowCount 0
		execute "CRUD.new"
		setValue "name", "TIGRAN PETROSIAN"
		setValue "nickname.nickname", "POSITIONAL GAMER"
		execute "CRUD.save"
		assertMessage "User with nickname created successfully"
		execute "CRUD.new"
		setValue "name", "VLADIMIR KRAMNIK"
		setValue "nickname.nickname", "POSITIONAL GAMER"
		execute "CRUD.save"
		assertError "The nickname is already registered"
		setValue "nickname.nickname", "POSITIONAL GAMER III"
		execute "CRUD.save"
		assertNoErrors()
		execute "Mode.list"
		assertListRowCount 2
		execute "List.viewDetail", "row=1"
		assertValue "name", "VLADIMIR KRAMNIK"
		setValue "nickname.nickname", "POSITIONAL GAMER"
		execute "CRUD.save"
		assertError "The nickname is already registered"		
		execute "Mode.list"
		checkAll()
		execute "CRUD.deleteSelected"
		assertNoErrors()
		assertListRowCount 0
		changeModule "Nickname"
		checkAll()
		execute "CRUD.deleteSelected"
	}
}
