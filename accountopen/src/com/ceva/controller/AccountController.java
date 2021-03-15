package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.AccountBean;
import com.ceva.bank.common.beans.IdTypeByCategory;
import com.ceva.bank.common.beans.OtpBean;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AccountService;
import com.nbk.util.CommonUtil;

@Controller
@Scope("prototype")
public class AccountController {

	@Autowired
	private AccountService accountService;

	private static final Logger log = Logger.getLogger(CommonController.class);

	@RequestMapping(value = "/account/loadparameters.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadParameters(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadParameters();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadbranches.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8", "application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBranches(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadBranches();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadcountries.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadCountries(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadCountries();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadstates.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadStates(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadStates();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadcities.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadCities(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadCities();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadidcate.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadIdentityCategories(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadIdentityCategories();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadidtypebycate.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadIdentityTypeByCategories(@RequestBody @Valid IdTypeByCategory category,
			BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		log.info(category.toString());
		log.info(bindingResult.toString());
		if (!bindingResult.hasErrors()) {
			responseDTO = accountService.loadIdentityTypeByCategories(category.getCatId());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/account/loadnationalities.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadNationalities(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadNationalities();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadmaritalstatuses.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadMaritalStasuses(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadMaritalStasuses();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadoccupations.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadOccupations(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadOccupations();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/account/loadsalutations.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadSalutaions(@RequestBody @Valid OtpBean bean, BindingResult bindingResult) {
		ResponseDTO responseDTO = null;
		responseDTO = accountService.loadSalutaions();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value =
	 * "/account/createaccount.action/{channel}/{userId}/{phone}/{dob}/{email}/{firstName}/{lastName}/{midName}/{maritalStatus}/{gender}/{state}/{city}/{country}/{address}/{houseNumber}/{occupation}/{streetName}/{salutation}/{nationality}/{idNumber}/{idType}/{idRefNumber}/{solId}/{pin}/{otp}",
	 * method = RequestMethod.POST, produces =
	 * {"application/json ; charset=utf-8"})
	 */
	@RequestMapping(value = "/account/createaccount.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> openAccount(@RequestBody @Valid AccountBean bean, BindingResult bindingResult) {
		log.debug(bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		if (!bindingResult.hasErrors()) {
			responseDTO = accountService.openAccount(bean);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
	}

}
